package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.ChatMessage;
import finalProject.fishingLogTracker.fishingTracker.entity.Message;
import finalProject.fishingLogTracker.fishingTracker.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    private User user1;
    private User user2;
    private Message message;
    private ChatMessage chatMessage;

    @BeforeEach
    void setUp() {
        // Create test users
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        // Create message
        message = Message.builder()
                .id(1L)
                .sender(user1)
                .receiver(user2)
                .content("Hello!")
                .sentAt(LocalDateTime.now())
                .build();

        // Create chat message
        chatMessage = new ChatMessage();
        chatMessage.setSender("user1");
        chatMessage.setReceiver("user2");
        chatMessage.setContent("Hello!");
        chatMessage.setTimestamp(LocalDateTime.now());
    }

    @Test
    void save_ShouldSaveMessageAndReturnChatMessage() {
        // Arrange
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Act
        ChatMessage result = messageService.save(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals(chatMessage.getSender(), result.getSender());
        assertEquals(chatMessage.getReceiver(), result.getReceiver());
        assertEquals(chatMessage.getContent(), result.getContent());
    }

    @Test
    void save_ShouldThrowException_WhenSenderNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            chatMessage.setSender("nonexistent");
            messageService.save(chatMessage);
        });
    }

    @Test
    void getConversation_ShouldReturnListOfMessages() {
        // Arrange
        List<Message> messages = Arrays.asList(message);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderBySentAtAsc(user1, user2, user1, user2))
                .thenReturn(messages);

        // Act
        List<ChatMessage> result = messageService.getConversation("user1", "user2");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getSender());
        assertEquals("user2", result.get(0).getReceiver());
        assertEquals("Hello!", result.get(0).getContent());
    }

    @Test
    void getConversation_ShouldReturnEmptyList_WhenNoMessages() {
        // Arrange
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderBySentAtAsc(user1, user2, user1, user2))
                .thenReturn(List.of());

        // Act
        List<ChatMessage> result = messageService.getConversation("user1", "user2");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}