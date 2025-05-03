package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;

import java.util.List;

public record BaitResponseWithCatches(
        Long id,
        BaitType baitType,
        String description,
        List<CatchResponse> catches
) {
}
