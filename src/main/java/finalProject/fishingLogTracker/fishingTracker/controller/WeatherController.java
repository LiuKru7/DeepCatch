package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.entity.Weather;
import finalProject.fishingLogTracker.fishingTracker.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor

public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public Weather getWeather(@RequestParam Double latitude, @RequestParam Double longitude) {
        return weatherService.fetchWeather(latitude, longitude);
    }
}