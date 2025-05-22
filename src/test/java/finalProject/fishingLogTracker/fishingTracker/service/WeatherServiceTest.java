package finalProject.fishingLogTracker.fishingTracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.entity.Weather;
import finalProject.fishingLogTracker.fishingTracker.exception.WeatherNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private WeatherService weatherService;

    private ObjectMapper objectMapper;
    private String validWeatherJson;
    private String invalidWeatherJson;

    @BeforeEach
    void setUp() {
        // Create service instance
        weatherService = new WeatherService();

        // Set up test values
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(weatherService, "apiUrl", "http://test-weather-api.com");

        // Replace the RestTemplate field with our mock
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);

        objectMapper = new ObjectMapper();

        // Create a valid weather JSON response
        validWeatherJson = """
                {
                    "main": {
                        "temp": 20.5,
                        "feels_like": 19.8,
                        "humidity": 65.0,
                        "pressure": 1015
                    },
                    "weather": [
                        {
                            "description": "clear sky"
                        }
                    ],
                    "wind": {
                        "speed": 5.2,
                        "deg": 180
                    }
                }
                """;

        // Create an invalid weather JSON response (missing main data)
        invalidWeatherJson = """
                {
                    "weather": [
                        {
                            "description": "clear sky"
                        }
                    ],
                    "wind": {
                        "speed": 5.2,
                        "deg": 180
                    }
                }
                """;
    }

    @Test
    void fetchWeather_ShouldReturnWeatherData_WhenValidResponse() throws Exception {
        // Arrange
        JsonNode rootNode = objectMapper.readTree(validWeatherJson);
        ResponseEntity<JsonNode> response = ResponseEntity.ok(rootNode);

        // Mock the RestTemplate to return our test response
        when(restTemplate.getForEntity(any(String.class), eq(JsonNode.class))).thenReturn(response);

        // Act
        Weather result = weatherService.fetchWeather(55.7, 24.3);

        // Assert
        assertNotNull(result);
        assertEquals(20.5, result.getTemperature());
        assertEquals(19.8, result.getFeelsLike());
        assertEquals(65.0, result.getHumidity());
        assertEquals("clear sky", result.getCondition());
        assertEquals(5.2, result.getWindSpeed());
        assertEquals(180, result.getWindDirection());
        assertEquals(1015, result.getPressure());
    }

    @Test
    void fetchWeather_ShouldThrowException_WhenInvalidResponse() throws Exception {
        // Arrange
        JsonNode rootNode = objectMapper.readTree(invalidWeatherJson);
        ResponseEntity<JsonNode> response = ResponseEntity.ok(rootNode);

        // Mock the RestTemplate to return our test response
        when(restTemplate.getForEntity(any(String.class), eq(JsonNode.class))).thenReturn(response);

        // Act & Assert
        assertThrows(WeatherNotFoundException.class, () -> weatherService.fetchWeather(55.7, 24.3));
    }

    @Test
    void fetchWeather_ShouldThrowException_WhenApiCallFails() {
        // Arrange
        when(restTemplate.getForEntity(any(String.class), eq(JsonNode.class)))
                .thenThrow(new RuntimeException("API call failed"));

        // Act & Assert
        assertThrows(WeatherNotFoundException.class, () -> weatherService.fetchWeather(55.7, 24.3));
    }
}