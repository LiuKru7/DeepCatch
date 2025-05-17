package finalProject.fishingLogTracker.fishingTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank
        @Size(min = 3, max = 20)
        String username,
        @NotBlank
        @Size(min = 3, max = 20)
        String firstName,
        @NotBlank
        @Size(min = 3, max = 20)
        String lastName,
        @Email
        String email

) {
}
