package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.auth.service.JwtService;
import finalProject.fishingLogTracker.fishingTracker.dto.UpdateProfileRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static finalProject.fishingLogTracker.auth.enums.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private CatchRepository catchRepository;

    @Autowired
    private JwtService jwtService;

    private User testUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Clean up existing data in correct order to respect foreign key constraints
        catchRepository.deleteAll();
        friendshipRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser.setFirstname("Test");
        testUser.setLastname("User");
        testUser.setRole(ROLE_USER);
        testUser = userRepository.save(testUser);

        // Generate JWT token
        jwtToken = jwtService.generateToken(testUser);
    }

    @AfterEach
    void tearDown() {
        // Clean up in correct order to respect foreign key constraints
        catchRepository.deleteAll();
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getProfileInfo_shouldReturnUserProfile() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/profile")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse.class);

        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.firstname()).isEqualTo("Test");
        assertThat(response.lastname()).isEqualTo("User");
    }

    @Test
    void getProfileInfo_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changeProfilePhoto_shouldUpdateUserPhoto() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes());

        // When
        MvcResult result = mockMvc.perform(multipart(HttpMethod.PUT, "/api/profile")
                .file(file)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse.class);

        assertThat(response.photoUrl()).isNotNull();
    }

    @Test
    void changeProfilePhoto_shouldReturnBadRequest_whenNoFileProvided() throws Exception {
        // When/Then
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/profile")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfile_shouldUpdateUserProfile() throws Exception {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest(
                "newusername",
                "New",
                "User",
                "new@example.com");

        // When
        MvcResult result = mockMvc.perform(put("/api/profile/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse.class);

        assertThat(response.username()).isEqualTo("newusername");
        assertThat(response.email()).isEqualTo("new@example.com");
        assertThat(response.firstname()).isEqualTo("New");
        assertThat(response.lastname()).isEqualTo("User");
    }

    @Test
    void updateProfile_shouldReturnBadRequest_whenInvalidData() throws Exception {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest(
                "u", // too short username
                "F", // too short first name
                "L", // too short last name
                "invalid-email" // invalid email
        );

        // When/Then
        mockMvc.perform(put("/api/profile/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfile_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest(
                "newusername",
                "New",
                "User",
                "new@example.com");

        // When/Then
        mockMvc.perform(put("/api/profile/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}