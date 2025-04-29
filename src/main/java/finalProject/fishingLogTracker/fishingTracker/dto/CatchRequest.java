package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;

import java.time.LocalDateTime;

public record CatchRequest(
        Long speciesId,
        Double size,
        Double weight,
        Long aquaticId,
        FishingStyle fishingStyle,
        LocalDateTime time,
        Long baitId,
        String photoUrl,
        Boolean isReleased,
        String description,
        Long userId
        ){
}
