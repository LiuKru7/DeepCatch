package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AquaticRequest(
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Name can only contain letters, spaces, and hyphens") String name,

        @NotNull(message = "Aquatic type cannot be null") AquaticType aquaticType) {
}
