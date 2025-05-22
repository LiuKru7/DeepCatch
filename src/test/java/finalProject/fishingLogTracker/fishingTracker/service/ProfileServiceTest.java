package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.UpdateProfileRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ProfileService profileService;

    private User user;
    private UserResponse userResponse;
    private UpdateProfileRequest updateRequest;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        // Create test user
        user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@email.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPhotoUrl("old-photo.jpg");

        // Create user response
        userResponse = new UserResponse(
                1L,
                "user1",
                "user1@email.com",
                "old-photo.jpg",
                "John",
                "Doe");

        // Create update request
        updateRequest = new UpdateProfileRequest(
                "newuser",
                "new@email.com",
                "New",
                "Name");

        // Create test file
        file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes());
    }

    @Test
    void changeProfilePhoto_ShouldUpdatePhotoAndReturnUserResponse() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(imageService.saveFile(any(MultipartFile.class))).thenReturn("new-photo.jpg");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userResponse);

        // Act
        UserResponse result = profileService.changeProfilePhoto(1L, file);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse, result);
        verify(userRepository).save(user);
        assertEquals("new-photo.jpg", user.getPhotoUrl());
    }

    @Test
    void changeProfilePhoto_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> profileService.changeProfilePhoto(1L, file));
    }

    @Test
    void getProfileInfo_ShouldReturnUserResponse() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userResponse);

        // Act
        UserResponse result = profileService.getProfileInfo(1L);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse, result);
    }

    @Test
    void getProfileInfo_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> profileService.getProfileInfo(1L));
    }

    @Test
    void updateProfile_ShouldUpdateUserAndReturnUserResponse() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userResponse);

        // Act
        UserResponse result = profileService.updateProfile(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse, result);
        verify(userRepository).save(user);
        assertEquals(updateRequest.username(), user.getUsername());
        assertEquals(updateRequest.email(), user.getEmail());
        assertEquals(updateRequest.firstName(), user.getFirstname());
        assertEquals(updateRequest.lastName(), user.getLastname());
    }

    @Test
    void updateProfile_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> profileService.updateProfile(1L, updateRequest));
    }
}