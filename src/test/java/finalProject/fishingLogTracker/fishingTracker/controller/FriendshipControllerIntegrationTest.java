package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.auth.service.JwtService;
import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static finalProject.fishingLogTracker.auth.enums.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FriendshipControllerIntegrationTest {

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
    private User otherUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Clean up existing data in correct order
        catchRepository.deleteAll(); // Delete catches first
        friendshipRepository.deleteAll(); // Then friendships
        userRepository.deleteAll(); // Finally users

        // Create test users
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(ROLE_USER);
        testUser = userRepository.save(testUser);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setPassword("password");
        otherUser.setRole(ROLE_USER);
        otherUser = userRepository.save(otherUser);

        // Generate JWT token for the test user
        jwtToken = jwtService.generateToken(testUser);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test in correct order
        catchRepository.deleteAll(); // Delete catches first
        friendshipRepository.deleteAll(); // Then friendships
        userRepository.deleteAll(); // Finally users
    }

    @Test
    void getMyFriends_shouldReturnEmptyList_whenNoFriends() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/friendship/friends")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse[].class);

        assertThat(responses).isEmpty();
    }

    @Test
    void getAllUsers_shouldReturnOtherUsers() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/friendship/userlist")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<String> usernames = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                List.class);

        assertThat(usernames).hasSize(1);
        assertThat(usernames).contains("otheruser");
        assertThat(usernames).doesNotContain("testuser");
    }

    @Test
    void addToFriend_shouldCreateFriendshipRequest() throws Exception {
        // When
        MvcResult result = mockMvc.perform(post("/api/friendship")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(otherUser.getUsername())) // Send username directly without JSON wrapping
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        FriendshipResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                FriendshipResponse.class);

        assertThat(response.senderUsername()).isEqualTo("testuser");
        assertThat(response.receiverUsername()).isEqualTo("otheruser");
        assertThat(response.status()).isEqualTo(FriendshipStatus.PENDING);

        // Verify database state
        assertThat(friendshipRepository.findBySenderIdAndReceiverIdAndStatus(
                testUser.getId(), otherUser.getId(), FriendshipStatus.PENDING))
                .isPresent();
    }

    @Test
    void getPendingRequestsReceived_shouldReturnReceivedRequests() throws Exception {
        // Given
        Friendship friendship = Friendship.builder()
                .sender(otherUser)
                .receiver(testUser)
                .status(FriendshipStatus.PENDING)
                .build();
        friendshipRepository.save(friendship);

        // When
        MvcResult result = mockMvc.perform(get("/api/friendship/received")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse[].class);

        assertThat(responses).hasSize(1);
        assertThat(responses[0].username()).isEqualTo("otheruser");
    }

    @Test
    void getPendingRequestsSent_shouldReturnSentRequests() throws Exception {
        // Given
        Friendship friendship = Friendship.builder()
                .sender(testUser)
                .receiver(otherUser)
                .status(FriendshipStatus.PENDING)
                .build();
        friendshipRepository.save(friendship);

        // When
        MvcResult result = mockMvc.perform(get("/api/friendship/sent")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        UserResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse[].class);

        assertThat(responses).hasSize(1);
        assertThat(responses[0].username()).isEqualTo("otheruser");
    }

    @Test
    void acceptRequest_shouldUpdateFriendshipStatus() throws Exception {
        // Given
        Friendship friendship = Friendship.builder()
                .sender(otherUser)
                .receiver(testUser)
                .status(FriendshipStatus.PENDING)
                .build();
        friendshipRepository.save(friendship);

        // When
        MvcResult result = mockMvc.perform(post("/api/friendship/accept")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(otherUser.getId())))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        FriendshipResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                FriendshipResponse.class);

        assertThat(response.senderUsername()).isEqualTo("otheruser");
        assertThat(response.receiverUsername()).isEqualTo("testuser");
        assertThat(response.status()).isEqualTo(FriendshipStatus.ACCEPTED);

        // Verify database state
        assertThat(friendshipRepository.findBySenderIdAndReceiverIdAndStatus(
                otherUser.getId(), testUser.getId(), FriendshipStatus.ACCEPTED))
                .isPresent();
    }

    @Test
    void deleteFriend_shouldRemoveFriendship() throws Exception {
        // Given
        Friendship friendship = Friendship.builder()
                .sender(testUser)
                .receiver(otherUser)
                .status(FriendshipStatus.ACCEPTED)
                .build();
        friendshipRepository.save(friendship);

        // When
        mockMvc.perform(delete("/api/friendship/{friendId}", otherUser.getId())
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        assertThat(friendshipRepository.findByUsers(testUser.getId(), otherUser.getId()))
                .isEmpty();
    }
}