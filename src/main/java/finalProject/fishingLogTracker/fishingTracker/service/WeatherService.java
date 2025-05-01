package finalProject.fishingLogTracker.fishingTracker.service;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.entity.Weather;
import finalProject.fishingLogTracker.fishingTracker.exception.WeatherNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    // RestTemplate is used for making HTTP requests
    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public Weather fetchWeather(Double latitude, Double longitude) {
        // Format the URL using the API base URL, latitude, longitude, and API key
        String urlString = String.format(
                "%s?lat=%s&lon=%s&units=metric&appid=%s",
                apiUrl, latitude, longitude, apiKey
        );

        try {
            log.info("Fetching weather data from URL: {}", urlString);

            // Make the API call and get the response body as a JsonNode
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(urlString, JsonNode.class);

            // Get the actual response body (root node of the JSON)
            JsonNode root = response.getBody();

            // Check if the response body is null or incomplete; throw an exception if so
            if (root == null || root.path("main").isMissingNode()) {
                throw new WeatherNotFoundException("Received incomplete or invalid weather data.");
            }

            // Create and populate the Weather object from the JSON response
            Weather weather = new Weather();
            weather.setTemperature(root.path("main").path("temp").asDouble());  // Temperature in Celsius
            weather.setFeelsLike(root.path("main").path("feels_like").asDouble());  // "Feels like" temperature
            weather.setHumidity(root.path("main").path("humidity").asDouble());  // Humidity in percentage
            weather.setCondition(root.path("weather").get(0).path("description").asText());  // Weather description (e.g., "clear sky")
            weather.setWindSpeed(root.path("wind").path("speed").asDouble());  // Wind speed (m/s)
            weather.setWindDirection(root.path("wind").path("deg").asInt());  // Wind direction (degrees)
            weather.setPressure(root.path("main").path("pressure").asInt());  // Atmospheric pressure

            // Return the populated Weather object
            return weather;

        } catch (Exception e) {
            // Log the error details if the weather data could not be fetched
            log.error("Failed to fetch weather data for lat={}, lon={}", latitude, longitude);
            // Throw a custom exception that can be handled globally
            throw new WeatherNotFoundException("Failed to fetch weather data. Please check the API key or try again later.");
        }
    }
}
