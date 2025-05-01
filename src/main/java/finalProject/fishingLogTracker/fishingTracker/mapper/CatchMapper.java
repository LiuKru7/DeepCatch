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

    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    @Mapping(target = "country", source = "location.country")
    @Mapping(target = "region", source = "location.region")
    CatchResponse toCatchResponse(Catch catchEntity);

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "weather", ignore = true)
    Catch toCatch(CatchRequest catchRequest);
}
