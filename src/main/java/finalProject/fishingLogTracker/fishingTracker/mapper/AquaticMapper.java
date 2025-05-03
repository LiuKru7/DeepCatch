package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AquaticMapper {

    Aquatic toAquatic(AquaticRequest aquaticRequest);
    AquaticResponse toAquaticResponse(Aquatic aquatic);
}
