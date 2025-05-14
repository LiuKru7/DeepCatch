package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> changeProfilePhoto(
            @AuthenticationPrincipal User user,
            @RequestPart("file") MultipartFile file
    )  {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profileService.changeProfilePhoto(user.getId(), file));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getProfileInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getProfileInfo(user.getId()));
    }
}
