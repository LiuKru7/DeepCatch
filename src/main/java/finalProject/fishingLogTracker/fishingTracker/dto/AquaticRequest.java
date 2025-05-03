package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;

public record AquaticRequest(
        String name,
        AquaticType aquaticType
) {
}
