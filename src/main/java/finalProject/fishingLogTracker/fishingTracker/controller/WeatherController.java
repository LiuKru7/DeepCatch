package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.entity.Weather;
import finalProject.fishingLogTracker.fishingTracker.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Fetches weather information based on latitude and longitude.
     *
     * @param latitude  the latitude of the location
     * @param longitude the longitude of the location
     * @return weather data for the given coordinates
     */
    @GetMapping
    public ResponseEntity<Weather> getWeather(
            @RequestParam final Double latitude,
            @RequestParam final Double longitude) {
        log.info("Received weather request for lat={}, lon={}", latitude, longitude);
        Weather weather = weatherService.fetchWeather(latitude, longitude);
        return ResponseEntity.ok(weather);
    }
}
