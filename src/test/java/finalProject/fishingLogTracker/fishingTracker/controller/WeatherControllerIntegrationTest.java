package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.auth.service.JwtService;
import finalProject.fishingLogTracker.fishingTracker.entity.Weather;
import finalProject.fishingLogTracker.fishingTracker.repository.WeatherRepository;
import finalProject.fishingLogTracker.fishingTracker.service.WeatherService;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static finalProject.fishingLogTracker.auth.enums.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private CatchRepository catchRepository;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private WeatherService weatherService;

    private Weather testWeather;
    private User testUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Clean up existing data in correct order to respect foreign key constraints
        weatherRepository.deleteAll();
        catchRepository.deleteAll();
        friendshipRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(ROLE_USER);
        testUser = userRepository.save(testUser);

        // Generate JWT token
        jwtToken = jwtService.generateToken(testUser);

        // Create test weather data
        testWeather = new Weather();
        testWeather.setTemperature(20.5);
        testWeather.setFeelsLike(19.8);
        testWeather.setHumidity(65.0);
        testWeather.setCondition("clear sky");
        testWeather.setWindSpeed(5.2);
        testWeather.setWindDirection(180);
        testWeather.setPressure(1013);
    }

    @AfterEach
    void tearDown() {
        // Clean up in correct order to respect foreign key constraints
        weatherRepository.deleteAll();
        catchRepository.deleteAll();
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getWeather_shouldReturnWeatherData() throws Exception {
        // Given
        when(weatherService.fetchWeather(anyDouble(), anyDouble()))
                .thenReturn(testWeather);

        // When
        MvcResult result = mockMvc.perform(get("/api/weather")
                .header("Authorization", "Bearer " + jwtToken)
                .param("latitude", "54.6872")
                .param("longitude", "25.2797")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Weather response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Weather.class);

        assertThat(response.getTemperature()).isEqualTo(20.5);
        assertThat(response.getFeelsLike()).isEqualTo(19.8);
        assertThat(response.getHumidity()).isEqualTo(65.0);
        assertThat(response.getCondition()).isEqualTo("clear sky");
        assertThat(response.getWindSpeed()).isEqualTo(5.2);
        assertThat(response.getWindDirection()).isEqualTo(180);
        assertThat(response.getPressure()).isEqualTo(1013);
    }

    @Test
    void getWeather_shouldReturnNotFound_whenWeatherServiceThrowsException() throws Exception {
        // Given
        when(weatherService.fetchWeather(anyDouble(), anyDouble()))
                .thenThrow(new RuntimeException("Failed to fetch weather data"));

        // When/Then
        mockMvc.perform(get("/api/weather")
                .header("Authorization", "Bearer " + jwtToken)
                .param("latitude", "54.6872")
                .param("longitude", "25.2797")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeather_shouldReturnBadRequest_whenCoordinatesAreInvalid() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/weather")
                .header("Authorization", "Bearer " + jwtToken)
                .param("latitude", "invalid")
                .param("longitude", "25.2797")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeather_shouldReturnBadRequest_whenLatitudeIsMissing() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/weather")
                .header("Authorization", "Bearer " + jwtToken)
                .param("longitude", "25.2797")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeather_shouldReturnBadRequest_whenLongitudeIsMissing() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/weather")
                .header("Authorization", "Bearer " + jwtToken)
                .param("latitude", "54.6872")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeather_shouldReturnBadRequest_whenBothCoordinatesAreMissing() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/weather")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeather_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/weather")
                .param("latitude", "54.6872")
                .param("longitude", "25.2797")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}