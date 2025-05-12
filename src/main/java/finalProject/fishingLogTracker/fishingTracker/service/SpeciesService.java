package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.entity.Bait;
import finalProject.fishingLogTracker.fishingTracker.entity.Species;
import finalProject.fishingLogTracker.fishingTracker.exception.AquaticNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.SpeciesNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.SpeciesMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.SpeciesRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final SpeciesMapper speciesMapper;

    public List<SpeciesResponse> getAllSpecies() {
        return speciesRepository.findAll().stream()
                .map(speciesMapper::toSpeciesResponse)
                .toList();
    }

    public SpeciesResponse addNewSpecies(SpeciesRequest speciesRequest) {
        Species species = speciesRepository.save(speciesMapper.toSpecies(speciesRequest));
        return speciesMapper.toSpeciesResponse(species);
    }

    public SpeciesResponse updateSpecies(Long id, SpeciesRequest speciesRequest) {
        log.info("Updating Species with ID: {}", id);
        Species existingSpecies = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException("Species not found with id: " + id));

        Species updatedSpecies = speciesMapper.toSpecies(speciesRequest);
        updatedSpecies.setId(existingSpecies.getId());
        Species saved = speciesRepository.save(updatedSpecies);

        return speciesMapper.toSpeciesResponse(saved);
    }

    public void deleteSpecies(Long id) {
        log.info("Deleting Species with ID: {}", id);

        if (!speciesRepository.existsById(id)) {
            throw new SpeciesNotFoundException("Species not found with id: " + id);
        }

        speciesRepository.deleteById(id);
    }
}
