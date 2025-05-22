package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.FriendshipResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Friendship;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.mapper.FriendshipMapper;
import finalProject.fishingLogTracker.fishingTracker.mapper.UserMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.FriendshipRepository;
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
class FriendshipServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private FriendshipRepository friendshipRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendshipMapper friendshipMapper;

    @InjectMocks
    private FriendshipService friendshipService;

    private User user1;
    private User user2;
    private UserResponse userResponse1;
    private UserResponse userResponse2;
    private Friendship friendship;
    private FriendshipResponse friendshipResponse;

    @BeforeEach
    void setUp() {
        // Create test users
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@email.com");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1.setPhotoUrl("photo1.jpg");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@email.com");
        user2.setFirstname("Jane");
        user2.setLastname("Smith");
        user2.setPhotoUrl("photo2.jpg");

        // Create user responses
        userResponse1 = new UserResponse(1L, "user1", "user1@email.com", "photo1.jpg", "John", "Doe");
        userResponse2 = new UserResponse(2L, "user2", "user2@email.com", "photo2.jpg", "Jane", "Smith");

        // Create friendship
        friendship = Friendship.builder()
                .id(1L)
                .sender(user1)
                .receiver(user2)
                .status(FriendshipStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // Create friendship response
        friendshipResponse = new FriendshipResponse(
                "user1",
                "user2",
                FriendshipStatus.PENDING);
    }

    @Test
    void getFriends_ShouldReturnListOfFriends() {
        // Arrange
        List<Friendship> friendships = Arrays.asList(friendship);
        when(friendshipRepository.findAcceptedFriendshipsForUser(1L)).thenReturn(friendships);
        when(userMapper.toUserDto(user2)).thenReturn(userResponse2);

        // Act
        List<UserResponse> result = friendshipService.getFriends(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponse2, result.get(0));
    }

    @Test
    void getPendingRequestsReceived_ShouldReturnListOfPendingRequests() {
        // Arrange
        List<Friendship> friendships = Arrays.asList(friendship);
        when(friendshipRepository.findByReceiverIdAndStatus(2L, FriendshipStatus.PENDING)).thenReturn(friendships);
        when(userMapper.toUserDto(user1)).thenReturn(userResponse1);

        // Act
        List<UserResponse> result = friendshipService.getPendingRequestsReceived(2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponse1, result.get(0));
    }

    @Test
    void getPendingRequestsSent_ShouldReturnListOfSentRequests() {
        // Arrange
        List<Friendship> friendships = Arrays.asList(friendship);
        when(friendshipRepository.findBySenderIdAndStatus(1L, FriendshipStatus.PENDING)).thenReturn(friendships);
        when(userMapper.toUserDto(user2)).thenReturn(userResponse2);

        // Act
        List<UserResponse> result = friendshipService.getPendingRequestsSent(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponse2, result.get(0));
    }

    @Test
    void sentFriendshipRequest_ShouldCreateNewFriendship() {
        // Arrange
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(friendshipRepository.existsBySenderIdAndReceiverIdOrViceVersa(1L, 2L)).thenReturn(false);
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);
        when(friendshipMapper.toFriendshipResponse(friendship)).thenReturn(friendshipResponse);

        // Act
        FriendshipResponse result = friendshipService.sentFriendshipRequest(1L, "user2");

        // Assert
        assertNotNull(result);
        assertEquals(friendshipResponse, result);
    }

    @Test
    void sentFriendshipRequest_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> friendshipService.sentFriendshipRequest(1L, "nonexistent"));
    }

    @Test
    void acceptRequest_ShouldUpdateFriendshipStatus() {
        // Arrange
        when(friendshipRepository.findBySenderIdAndReceiverIdAndStatus(2L, 1L, FriendshipStatus.PENDING))
                .thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);
        when(friendshipMapper.toFriendshipResponse(friendship)).thenReturn(friendshipResponse);

        // Act
        FriendshipResponse result = friendshipService.acceptRequest(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(friendshipResponse, result);
        assertEquals(FriendshipStatus.ACCEPTED, friendship.getStatus());
    }

    @Test
    void deleteFriend_ShouldDeleteFriendship() {
        // Arrange
        when(friendshipRepository.findByUsers(1L, 2L)).thenReturn(Optional.of(friendship));

        // Act
        friendshipService.deleteFriend(1L, 2L);

        // Assert
        verify(friendshipRepository).delete(friendship);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsernames() {
        // Arrange
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<String> result = friendshipService.getAllUsers(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user2", result.get(0));
    }
}