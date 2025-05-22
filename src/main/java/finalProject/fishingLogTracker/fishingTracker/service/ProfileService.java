package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.UpdateProfileRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;

    public UserResponse changeProfilePhoto(Long id, MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String url = imageService.saveFile(file);
        user.setPhotoUrl(url);

        return userMapper.toUserDto(userRepository.save(user));
    }


    public UserResponse getProfileInfo(Long id) {
        return userMapper.toUserDto(userRepository
                .findById(id).orElseThrow(()-> new RuntimeException("User not found")));
    }

    public UserResponse updateProfile(Long id, UpdateProfileRequest request) {
        var user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        user.setEmail(request.email());
        user.setFirstname(request.firstName());
        user.setLastname(request.lastName());
        user.setUsername(request.username());
        return userMapper.toUserDto(userRepository.save(user));
    }
}
