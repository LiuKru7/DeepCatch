package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.*;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import finalProject.fishingLogTracker.fishingTracker.exception.AquaticNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.BaitNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.CatchNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.SpeciesNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.CatchMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

        public CatchResponse addCatch(CatchRequest catchRequest) {
                log.info("Creating new Catch : {}", catchRequest);
                Catch catchEntity = catchMapper.toCatch(catchRequest);

                var aquatic = aquaticRepository.findById(catchRequest.aquaticId())
                                .orElseThrow(() -> new AquaticNotFoundException("Aquatic not found"));
                Bait bait = baitRepository.findById(catchRequest.baitId())
                                .orElseThrow(() -> new BaitNotFoundException("Bait not found"));
                Species species = speciesRepository.findById(catchRequest.speciesId())
                                .orElseThrow(() -> new SpeciesNotFoundException("Species not found"));
                User user = userRepository.findById(catchRequest.userId())
                                .orElseThrow(() -> new EntityNotFoundException("User not found"));

                Location savedLocation = getLocation(catchRequest);
                Weather savedWeather = getWeatherForLocation(savedLocation);

                catchEntity.setSpecies(species);
                catchEntity.setUser(user);
                catchEntity.setBait(bait);
                catchEntity.setLocation(savedLocation);
                ;
                catchEntity.setWeather(savedWeather);
                catchEntity.setAquatic(aquatic);

                Catch savedCatch = catchRepository.save(catchEntity);
                return catchMapper.toCatchResponse(savedCatch);
        }

        private Weather getWeatherForLocation(Location savedLocation) {
                Weather weather = weatherService.fetchWeather(savedLocation.getLatitude(),
                                savedLocation.getLongitude());
                return weatherRepository.save(weather);
        }

        private Location getLocation(final CatchRequest catchRequest) {
                final Double latitude = catchRequest.latitude() != null ? catchRequest.latitude() : 55.3328;
                final Double longitude = catchRequest.longitude() != null ? catchRequest.longitude() : 26.0606;
                final String country = catchRequest.country() != null ? catchRequest.country() : "Lithuania";
                final String district = catchRequest.district() != null ? catchRequest.district() : "Lazdijų Rajonas";

                Location location = new Location(null, latitude, longitude, country, district);
                return locationRepository.save(location);
        }

        public CatchResponse getCatchById(Long id) {
                log.info("Fetching Catch with ID: {}", id);
                Catch catchEntity = catchRepository.findById(id)
                                .orElseThrow(() -> new CatchNotFoundException("Catch not found with id: " + id));
                return catchMapper.toCatchResponse(catchEntity);
        }

        public List<CatchResponse> getAllCatches() {
                log.info("Fetching all Catch entries");
                return catchRepository.findAll()
                                .stream()
                                .map(catchMapper::toCatchResponse)
                                .toList();
        }

        public CatchResponse updateCatch(Long id, CatchRequest catchRequest) {
                log.info("Updating Catch with ID: {}", id);
                Catch existingCatch = catchRepository.findById(id)
                                .orElseThrow(() -> new CatchNotFoundException("Catch not found with id: " + id));

                Catch updatedCatch = catchMapper.toCatch(catchRequest);
                updatedCatch.setId(existingCatch.getId());
                Catch saved = catchRepository.save(updatedCatch);

                return catchMapper.toCatchResponse(saved);
        }

        public void deleteCatch(Long id) {
                log.info("Deleting Catch with ID: {}", id);

                if (!catchRepository.existsById(id)) {
                        throw new CatchNotFoundException("Catch not found with id: " + id);
                }

                catchRepository.deleteById(id);
        }

        public List<CatchResponse> getCatchesByFishingStyle(final FishingStyle style) {
                var catches = catchRepository.findByFishingStyle(style);
                return catches.stream()
                                .map(catchMapper::toCatchResponse)
                                .toList();
        }

        public List<CatchResponse> getCatchesByBait(final BaitType baitType) {
                var catches = catchRepository.findByBait_BaitType(baitType);
                return catches.stream()
                                .map(catchMapper::toCatchResponse)
                                .toList();
        }

        public List<CatchResponse> getCatchesByUser(Long userId) {
                log.info("Fetching catches for user ID: {}", userId);
                var catches = catchRepository.findByUserId(userId);
                return catches.stream()
                                .map(catchMapper::toCatchResponse)
                                .toList();
        }

        public CatchResponse addCatchWithPhoto(final CatchRequest catchRequest, final MultipartFile file) {
                log.info("Creating new Catch : {}", catchRequest);
                Catch catchEntity = catchMapper.toCatch(catchRequest);

                var aquatic = aquaticRepository.findById(catchRequest.aquaticId())
                                .orElseThrow(() -> new EntityNotFoundException("Aquatic not found"));
                Bait bait = baitRepository.findById(catchRequest.baitId())
                                .orElseThrow(() -> new EntityNotFoundException("Bait not found"));
                Species species = speciesRepository.findById(catchRequest.speciesId())
                                .orElseThrow(() -> new EntityNotFoundException("Species not found"));
                User user = userRepository.findById(catchRequest.userId())
                                .orElseThrow(() -> new EntityNotFoundException("User not found"));

                Location savedLocation = getLocation(catchRequest);
                Weather savedWeather = getWeatherForLocation(savedLocation);

                String fileName = imageService.saveFile(file);

                catchEntity.setSpecies(species);
                catchEntity.setUser(user);
                catchEntity.setBait(bait);
                catchEntity.setLocation(savedLocation);
                ;
                catchEntity.setWeather(savedWeather);
                catchEntity.setAquatic(aquatic);
                catchEntity.setPhotoUrl(fileName);

                Catch savedCatch = catchRepository.save(catchEntity);
                return catchMapper.toCatchResponse(savedCatch);
        }
}
