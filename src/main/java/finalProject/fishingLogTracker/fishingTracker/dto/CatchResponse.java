package finalProject.fishingLogTracker.fishingTracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;

import java.time.LocalDateTime;

public record CatchResponse(
                Long id,
                Long speciesId,
                String speciesName,
                String aquaticName,
                String speciesLatinName,
                AquaticType aquaticType,
                FishingStyle fishingStyle,
                String photoUrl,
                Double size,
                Double weight,
                Long aquaticId,
                LocalDateTime time,
                Long baitId,
                String baitDescription,
                BaitType baitType,
                Boolean isReleased,
                String description,
                Long userId,
                Double latitude,
                Double longitude,
                String country,
                String district) {
}
