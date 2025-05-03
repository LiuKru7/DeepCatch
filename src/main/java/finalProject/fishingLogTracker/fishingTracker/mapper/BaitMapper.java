package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Bait;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaitMapper {
    Bait toBait (BaitRequest baitRequest);
    BaitResponse toBaitResponse(Bait bait);
}
