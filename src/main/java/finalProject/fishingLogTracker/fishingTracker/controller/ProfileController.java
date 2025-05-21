package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.UpdateProfileRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.UserResponse;
import finalProject.fishingLogTracker.fishingTracker.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Changes the authenticated user's profile photo.
     *
     * @param user the authenticated user
     * @param file the new profile photo
     * @return the updated UserResponse
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> changeProfilePhoto(
            @AuthenticationPrincipal final User user,
            @RequestPart("file") final MultipartFile file) {
        log.info("User {} is updating profile photo", user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profileService.changeProfilePhoto(user.getId(), file));
    }

    /**
     * Retrieves the profile information of the authenticated user.
     *
     * @param user the authenticated user
     * @return the user's profile information
     */
    @GetMapping
    public ResponseEntity<UserResponse> getProfileInfo(@AuthenticationPrincipal final User user) {
        log.info("User {} requested their profile info", user.getId());
        return ResponseEntity.ok(profileService.getProfileInfo(user.getId()));
    }

    /**
     * Updates the profile data of the authenticated user.
     *
     * @param user    the authenticated user
     * @param request the updated profile data
     * @return the updated UserResponse
     */
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal final User user,
            @Valid @RequestBody final UpdateProfileRequest request) {
        log.info("User {} is updating profile info", user.getId());
        return ResponseEntity.ok(profileService.updateProfile(user.getId(), request));
    }
}
