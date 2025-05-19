package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.*;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import finalProject.fishingLogTracker.fishingTracker.exception.CatchNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.CatchMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CatchService {

    private final CatchRepository catchRepository;
    private final CatchMapper catchMapper;
    private final LocationRepository locationRepository;
    private final WeatherService weatherService;
    private final WeatherRepository weatherRepository;
    private final UserRepository userRepository;
    private final SpeciesRepository speciesRepository;
    private final BaitRepository baitRepository;
    private final AquaticRepository aquaticRepository;
    private final ImageService imageService;

    @Cacheable(value = "catches", key = "#id")
    public CatchResponse getCatchById(Long id) {
        log.info("Fetching Catch with ID: {}", id);
        Catch catchEntity = catchRepository.findById(id)
                .orElseThrow(() -> new CatchNotFoundException("Catch not found with id: " + id));
        return catchMapper.toCatchResponse(catchEntity);
    }

    @Cacheable("allCatches")
    public List<CatchResponse> getAllCatches() {
        log.info("Fetching all Catch entries");
        return catchRepository.findAll()
                .stream()
                .map(catchMapper::toCatchResponse)
                .toList();
    }

    @CacheEvict(value = {"catches", "allCatches", "userCatches", "styleCatches", "baitCatches"}, allEntries = true)
    public CatchResponse updateCatch(final Long id, final CatchRequest catchRequest) {
        log.info("Updating Catch with ID: {}", id);
        Catch existingCatch = catchRepository.findById(id)
                .orElseThrow(() -> new CatchNotFoundException("Catch not found with id: " + id));

        Catch updatedCatch = catchMapper.toCatch(catchRequest);
        updatedCatch.setId(existingCatch.getId());
        Catch saved = catchRepository.save(updatedCatch);
        return catchMapper.toCatchResponse(saved);
    }

    @CacheEvict(value = {"catches", "allCatches", "userCatches", "styleCatches", "baitCatches"}, allEntries = true)
    public void deleteCatch(final Long id) {
        log.info("Deleting Catch with ID: {}", id);

        if (!catchRepository.existsById(id)) {
            throw new CatchNotFoundException("Catch not found with id: " + id);
        }

        catchRepository.deleteById(id);
    }

    @Cacheable(value = "styleCatches", key = "#style")
    public List<CatchResponse> getCatchesByFishingStyle(final FishingStyle style) {
        log.info("Fetching catches by fishing style: {}", style);
        var catches = catchRepository.findByFishingStyle(style);
        return catches.stream()
                .map(catchMapper::toCatchResponse)
                .toList();
    }

    @Cacheable(value = "baitCatches", key = "#baitType")
    public List<CatchResponse> getCatchesByBait(final BaitType baitType) {
        log.info("Fetching catches by bait type: {}", baitType);
        var catches = catchRepository.findByBait_BaitType(baitType);
        return catches.stream()
                .map(catchMapper::toCatchResponse)
                .toList();
    }

    @Cacheable(value = "userCatches", key = "#userId")
    public List<CatchResponse> getCatchesByUser(final Long userId) {
        log.info("Fetching catches for user ID: {}", userId);
        var catches = catchRepository.findByUserId(userId);
        return catches.stream()
                .map(catchMapper::toCatchResponse)
                .toList();
    }

    @CacheEvict(value = {"catches", "allCatches", "userCatches", "styleCatches", "baitCatches"}, allEntries = true)
    public CatchResponse addNewCatch(final CatchRequest request, final MultipartFile file, final Long userId) {
        log.info("Creating new Catch: {}", request);

        Catch catchEntity = catchMapper.toCatch(request);
        fillRelatedEntities(catchEntity, request, userId);
        fillLocationAndWeather(catchEntity, request);
        attachPhoto(catchEntity, file);

        Catch savedCatch = catchRepository.save(catchEntity);
        return catchMapper.toCatchResponse(savedCatch);
    }

    @Cacheable(value = "userCatches", key = "#id")
    public List<CatchResponse> getCatchesByUserId(Long id) {
        log.info("Fetching catches for user ID: {}", id);
        List<Catch> catches = catchRepository.findByUserId(id);
        return catches.stream()
                .map(catchMapper::toCatchResponse)
                .toList();
    }

    private Weather getWeatherForLocation(Location savedLocation) {
        log.info("Fetching weather for location: ({}, {})",
                savedLocation.getLatitude(), savedLocation.getLongitude());
        Weather weather = weatherService.fetchWeather(savedLocation.getLatitude(),
                savedLocation.getLongitude());
        return weatherRepository.save(weather);
    }

    private Location getLocation(final CatchRequest request) {
        log.info("Saving location for request");
        final Double latitude = request.latitude();
        final Double longitude = request.longitude();
        final String country = request.country();
        final String district = request.district();
        Location location = new Location(null, latitude, longitude, country, district);
        return locationRepository.save(location);
    }

    private void fillRelatedEntities(Catch catchEntity, CatchRequest request, Long userId) {
        log.info("Binding related entities to catch");
        Aquatic aquatic = aquaticRepository.findById(request.aquaticId())
                .orElseThrow(() -> new EntityNotFoundException("Aquatic not found"));
        Bait bait = baitRepository.findById(request.baitId())
                .orElseThrow(() -> new EntityNotFoundException("Bait not found"));
        Species species = speciesRepository.findById(request.speciesId())
                .orElseThrow(() -> new EntityNotFoundException("Species not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        catchEntity.setSpecies(species);
        catchEntity.setBait(bait);
        catchEntity.setUser(user);
        catchEntity.setAquatic(aquatic);
    }

    private void fillLocationAndWeather(final Catch catchEntity, final CatchRequest request) {
        log.info("Saving location and weather");
        Location location = getLocation(request);
        Weather weather = getWeatherForLocation(location);

        catchEntity.setLocation(location);
        catchEntity.setWeather(weather);
    }

    private void attachPhoto(final Catch catchEntity, final MultipartFile file) {
        log.info("Saving photo");
        String fileName = imageService.saveFile(file);
        catchEntity.setPhotoUrl(fileName);
    }
}

