package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.auth.service.JwtService;
import finalProject.fishingLogTracker.fishingTracker.dto.ChatMessage;
import finalProject.fishingLogTracker.fishingTracker.entity.Message;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.MessageRepository;
import finalProject.fishingLogTracker.fishingTracker.service.MessageService;
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

import java.time.LocalDateTime;
import java.util.List;

import static finalProject.fishingLogTracker.auth.enums.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MessageControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private MessageRepository messageRepository;

        @Autowired
        private FriendshipRepository friendshipRepository;

        @Autowired
        private CatchRepository catchRepository;

        @Autowired
        private MessageService messageService;

        @Autowired
        private JwtService jwtService;

        private User testUser;
        private User friendUser;
        private String jwtToken;

        @BeforeEach
        void setUp() {
                // Clean up existing data in correct order
                messageRepository.deleteAll(); // Delete messages first
                catchRepository.deleteAll(); // Then catches
                friendshipRepository.deleteAll(); // Then friendships
                userRepository.deleteAll(); // Finally users

                // Create test users
                testUser = new User();
                testUser.setUsername("testuser");
                testUser.setPassword("password");
                testUser.setRole(ROLE_USER);
                testUser = userRepository.save(testUser);

                friendUser = new User();
                friendUser.setUsername("frienduser");
                friendUser.setPassword("password");
                friendUser.setRole(ROLE_USER);
                friendUser = userRepository.save(friendUser);

                // Generate JWT token for the test user
                jwtToken = jwtService.generateToken(testUser);
        }

        @AfterEach
        void tearDown() {
                // Clean up after each test in correct order
                messageRepository.deleteAll(); // Delete messages first
                catchRepository.deleteAll(); // Then catches
                friendshipRepository.deleteAll(); // Then friendships
                userRepository.deleteAll(); // Finally users
        }

        @Test
        void getConversation_shouldReturnMessagesBetweenUsers() throws Exception {
                // Given
                Message message1 = Message.builder()
                                .sender(testUser)
                                .receiver(friendUser)
                                .content("Hello friend!")
                                .sentAt(LocalDateTime.now())
                                .build();
                messageRepository.save(message1);

                Message message2 = Message.builder()
                                .sender(friendUser)
                                .receiver(testUser)
                                .content("Hi there!")
                                .sentAt(LocalDateTime.now())
                                .build();
                messageRepository.save(message2);

                // When
                MvcResult result = mockMvc.perform(get("/api/messages/{friendUsername}", friendUser.getUsername())
                                .header("Authorization", "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                ChatMessage[] responses = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                ChatMessage[].class);

                assertThat(responses).hasSize(2);
                assertThat(responses[0].getSender()).isEqualTo(testUser.getUsername());
                assertThat(responses[0].getReceiver()).isEqualTo(friendUser.getUsername());
                assertThat(responses[0].getContent()).isEqualTo("Hello friend!");
                assertThat(responses[1].getSender()).isEqualTo(friendUser.getUsername());
                assertThat(responses[1].getReceiver()).isEqualTo(testUser.getUsername());
                assertThat(responses[1].getContent()).isEqualTo("Hi there!");
        }

        @Test
        void getConversation_shouldReturnEmptyList_whenNoMessages() throws Exception {
                // When
                MvcResult result = mockMvc.perform(get("/api/messages/{friendUsername}", friendUser.getUsername())
                                .header("Authorization", "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                ChatMessage[] responses = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                ChatMessage[].class);

                assertThat(responses).isEmpty();
        }

        @Test
        void getConversation_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
                // When/Then
                mockMvc.perform(get("/api/messages/{friendUsername}", friendUser.getUsername())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void getConversation_shouldReturnMessagesInCorrectOrder() throws Exception {
                // Given
                Message message1 = Message.builder()
                                .sender(testUser)
                                .receiver(friendUser)
                                .content("First message")
                                .sentAt(LocalDateTime.now().minusMinutes(2))
                                .build();
                messageRepository.save(message1);

                Message message2 = Message.builder()
                                .sender(friendUser)
                                .receiver(testUser)
                                .content("Second message")
                                .sentAt(LocalDateTime.now().minusMinutes(1))
                                .build();
                messageRepository.save(message2);

                Message message3 = Message.builder()
                                .sender(testUser)
                                .receiver(friendUser)
                                .content("Third message")
                                .sentAt(LocalDateTime.now())
                                .build();
                messageRepository.save(message3);

                // When
                MvcResult result = mockMvc.perform(get("/api/messages/{friendUsername}", friendUser.getUsername())
                                .header("Authorization", "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                ChatMessage[] responses = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                ChatMessage[].class);

                assertThat(responses).hasSize(3);
                assertThat(responses[0].getContent()).isEqualTo("First message");
                assertThat(responses[1].getContent()).isEqualTo("Second message");
                assertThat(responses[2].getContent()).isEqualTo("Third message");
        }

        @Test
        void getConversation_shouldReturnEmptyList_whenFriendDoesNotExist() throws Exception {
                // When
                MvcResult result = mockMvc.perform(get("/api/messages/{friendUsername}", "nonexistentuser")
                                .header("Authorization", "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                ChatMessage[] responses = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                ChatMessage[].class);

                assertThat(responses).isEmpty();
        }

        @Test
        void getConversation_shouldReturnMessages_whenUsersAreFriends() throws Exception {
                // Given
                // Create friendship between users
                Friendship friendship = Friendship.builder()
                                .sender(testUser)
                                .receiver(friendUser)
                                .status(FriendshipStatus.ACCEPTED)
                                .createdAt(LocalDateTime.now())
                                .build();
                friendshipRepository.save(friendship);

                Message message = Message.builder()
                                .sender(testUser)
                                .receiver(friendUser)
                                .content("Hello friend!")
                                .sentAt(LocalDateTime.now())
                                .build();
                messageRepository.save(message);

                // When
                MvcResult result = mockMvc.perform(get("/api/messages/{friendUsername}", friendUser.getUsername())
                                .header("Authorization", "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                ChatMessage[] responses = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                ChatMessage[].class);

                assertThat(responses).hasSize(1);
                assertThat(responses[0].getSender()).isEqualTo(testUser.getUsername());
                assertThat(responses[0].getReceiver()).isEqualTo(friendUser.getUsername());
                assertThat(responses[0].getContent()).isEqualTo("Hello friend!");
        }

        @Test
        void getConversation_shouldReturnEmptyList_whenNotFriends() throws Exception {
                // When
                MvcResult result = mockMvc.perform(get("/api/messages/{friendUsername}", friendUser.getUsername())
                                .header("Authorization", "Bearer " + jwtToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                ChatMessage[] responses = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                ChatMessage[].class);

                assertThat(responses).isEmpty();
        }
}