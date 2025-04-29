package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Species;
import finalProject.fishingLogTracker.fishingTracker.mapper.SpeciesMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.SpeciesRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final SpeciesMapper speciesMapper;

    public List<SpeciesResponse> getAllSpecies() {
        return  speciesRepository.findAll().stream()
                .map(speciesMapper::toSpeciesResponse)
                .toList();

    }

    public SpeciesResponse addNewSpecies(SpeciesRequest speciesRequest) {
        Species species = speciesRepository.save(speciesMapper.toSpecies(speciesRequest));
        return speciesMapper.toSpeciesResponse(species);
    }
}
