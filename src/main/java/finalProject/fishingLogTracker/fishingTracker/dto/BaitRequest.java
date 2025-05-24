package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BaitRequest(
        @NotNull(message = "Bait type must not be null")
        @Schema(implementation = BaitType.class)
        BaitType baitType,

        @NotBlank(message = "Description must not be blank")
        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
        @Pattern(regexp = "^[a-zA-Z0-9\\s.,!?-]+$", message = "Description can only contain letters, numbers, spaces, and basic punctuation")
        String description) {
}
