package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatchMapper {

    @Mapping(target = "speciesId", source = "species.id")
    @Mapping(target = "aquaticId", source = "aquatic.id")
    @Mapping(target = "baitId", source = "bait.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "aquaticName", source = "aquatic.name")
    @Mapping(target = "aquaticType", source = "aquatic.aquaticType")
    @Mapping(target = "speciesName", source = "species.name")
    @Mapping(target = "speciesLatinName", source = "species.latinName")
    @Mapping(target = "baitType", source = "bait.baitType")
    @Mapping(target = "baitDescription", source = "bait.description")
    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    @Mapping(target = "country", source = "location.country")
    @Mapping(target = "district", source = "location.district")
    CatchResponse toCatchResponse(Catch catchEntity);

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "weather", ignore = true)
    Catch toCatch(CatchRequest catchRequest);
}
