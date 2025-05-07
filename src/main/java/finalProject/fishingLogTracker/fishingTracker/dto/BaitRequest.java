package finalProject.fishingLogTracker.fishingTracker.dto;

import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import io.swagger.v3.oas.annotations.media.Schema;

public record BaitRequest (
        @Schema(implementation = BaitType.class)
        BaitType baitType,
        String description
){
}
