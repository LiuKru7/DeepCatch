package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AquaticRequest(
        @NotBlank(message = "Name must not be blank")
        @Pattern(regexp = "[a-zA-Z\\s-]+", message = "Name can only contain letters, spaces, and hyphens")
        String name,

        @NotNull(message = "Aquatic type cannot be null")
        AquaticType aquaticType) {
}
