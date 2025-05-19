package finalProject.fishingLogTracker.fishingTracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import jakarta.validation.constraints.NotNull;

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
                Long userId,
                @NotNull
                Double latitude,
                Double longitude,
                String country,
                String district) {
}
