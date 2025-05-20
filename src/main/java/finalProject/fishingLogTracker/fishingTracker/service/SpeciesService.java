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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final SpeciesMapper speciesMapper;

    @Cacheable("species")
    public List<SpeciesResponse> getAllSpecies() {
        return speciesRepository.findAll().stream()
                .map(speciesMapper::toSpeciesResponse)
                .toList();
    }

    @Cacheable(value = "species", key = "#id")
    public SpeciesResponse getSpeciesById(Long id) {
        log.info("Fetching Species with ID: {}", id);
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException("Species not found with id: " + id));
        return speciesMapper.toSpeciesResponse(species);
    }

    @CacheEvict(value = "species", allEntries = true)
    public SpeciesResponse addNewSpecies(SpeciesRequest speciesRequest) {
        Species species = speciesRepository.save(speciesMapper.toSpecies(speciesRequest));
        return speciesMapper.toSpeciesResponse(species);
    }

    @CacheEvict(value = "species", allEntries = true)
    public SpeciesResponse updateSpecies(Long id, SpeciesRequest speciesRequest) {
        log.info("Updating Species with ID: {}", id);
        Species existingSpecies = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException("Species not found with id: " + id));

        Species updatedSpecies = speciesMapper.toSpecies(speciesRequest);
        updatedSpecies.setId(existingSpecies.getId());
        Species saved = speciesRepository.save(updatedSpecies);

        return speciesMapper.toSpeciesResponse(saved);
    }

    @CacheEvict(value = "species", allEntries = true)
    public void deleteSpecies(Long id) {
        log.info("Deleting Species with ID: {}", id);

        if (!speciesRepository.existsById(id)) {
            throw new SpeciesNotFoundException("Species not found with id: " + id);
        }

        speciesRepository.deleteById(id);
    }
}
