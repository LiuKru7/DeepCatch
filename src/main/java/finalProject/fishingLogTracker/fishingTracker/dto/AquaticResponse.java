package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;


public record AquaticResponse(
        Long id,
        String name,
        AquaticType aquaticType
) {
}
