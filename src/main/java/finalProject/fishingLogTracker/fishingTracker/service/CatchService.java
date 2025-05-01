package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
import finalProject.fishingLogTracker.fishingTracker.entity.Location;
import finalProject.fishingLogTracker.fishingTracker.entity.Weather;
import finalProject.fishingLogTracker.fishingTracker.exception.CatchNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.CatchMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.LocationRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.WeatherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public CatchResponse addCatch(CatchRequest catchRequest) throws IOException {
        log.info("Creating new Catch : {}", catchRequest);
        Catch catchEntity = catchMapper.toCatch(catchRequest);

        Location savedLocation = getLocation(catchRequest);
        Weather savedWeather = getWeatherForLocation(savedLocation);

        catchEntity.setLocation(savedLocation);
        catchEntity.setWeather(savedWeather);

        Catch savedCatch = catchRepository.save(catchEntity);
        return catchMapper.toCatchResponse(savedCatch);
    }

    private Weather getWeatherForLocation(Location savedLocation) {
        Weather weather = weatherService.fetchWeather(savedLocation.getLatitude(), savedLocation.getLongitude());
        return weatherRepository.save(weather);
    }

    private Location getLocation(CatchRequest catchRequest) {
        Double latitude = catchRequest.latitude() != null ? catchRequest.latitude() : 55.3328;
        Double longitude = catchRequest.longitude() != null ? catchRequest.longitude() : 26.0606;
        String country = catchRequest.country() != null ? catchRequest.country() : "Lithuania";
        String region = catchRequest.region() != null ? catchRequest.region() : "Lazdijų Rajonas";

        Location location = new Location(null, latitude, longitude, country, region);
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
}
