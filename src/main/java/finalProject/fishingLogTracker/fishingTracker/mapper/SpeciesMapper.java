package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Species;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {

    SpeciesResponse toSpeciesResponse(Species species);

    @Mapping(target = "id", ignore = true)
    Species toSpecies(SpeciesRequest speciesRequest);
}
