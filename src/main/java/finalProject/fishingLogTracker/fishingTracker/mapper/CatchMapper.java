package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatchMapper {

    @Mapping(target = "speciesId", source = "species.id")
    @Mapping(target = "aquaticId", source = "aquatic.id")
    @Mapping(target = "baitId", source = "bait.id")
    @Mapping(target = "userId", source = "user.id")
    CatchResponse toCatchResponse(Catch catchEntity);

    Catch toCatch (CatchRequest catchRequest);
}
