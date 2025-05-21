package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AquaticRequest(
                @NotBlank(message = "Name must not be blank") String name,
                @NotNull(message = "Aquatic type must not be null") AquaticType aquaticType) {
}
