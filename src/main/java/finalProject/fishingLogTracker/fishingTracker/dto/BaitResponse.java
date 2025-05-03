package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;

public record BaitResponse(
        Long id,
        BaitType baitType,
        String description
) {
}
