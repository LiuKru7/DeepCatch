package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;

public record BaitRequest (
        BaitType baitType,
        String description
){
}
