package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AquaticMapper {
    AquaticMapper INSTANCE = Mappers.getMapper(AquaticMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "aquaticType", source = "aquaticType")
    AquaticResponse toAquaticResponse(Aquatic aquatic);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "aquaticType", source = "aquaticType")
    Aquatic toAquatic(AquaticRequest aquaticRequest);
}
