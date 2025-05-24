package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.UpdateProfileRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.exception.UserNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;

    public UserResponse changeProfilePhoto(Long id, MultipartFile file) {
        log.info("Changing profile photo for user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        String url = imageService.saveFile(file);
        user.setPhotoUrl(url);

        log.info("Profile photo updated successfully for user ID: {}", id);
        return userMapper.toUserDto(userRepository.save(user));
    }

    public UserResponse getProfileInfo(Long id) {
        log.info("Fetching profile info for user ID: {}", id);
        UserResponse response = userMapper.toUserDto(userRepository
                .findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
        log.info("Profile info retrieved successfully for user ID: {}", id);
        return response;
    }

    public UserResponse updateProfile(Long id, UpdateProfileRequest request) {
        log.info("Updating profile for user ID: {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setEmail(request.email());
        user.setFirstname(request.firstName());
        user.setLastname(request.lastName());
        user.setUsername(request.username());
        log.info("Profile updated successfully for user ID: {}", id);
        return userMapper.toUserDto(userRepository.save(user));
    }
}
