package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.exception.AquaticNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.AquaticMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class AquaticService {

    private final AquaticRepository aquaticRepository;
    private final AquaticMapper aquaticMapper;

    /**
     * Retrieves all aquatic entries from the database.
     *
     * @return list of AquaticResponse DTOs
     */
    @Cacheable("aquatics")
    public List<AquaticResponse> getAllAquatics() {
        log.info("Fetching all aquatics");
        return aquaticRepository.findAll().stream()
                .map(aquaticMapper::toAquaticResponse)
                .toList();
    }

    /**
     * Adds a new aquatic entry and evicts the cache.
     *
     * @param aquaticRequest the aquatic data to be added
     * @return the created AquaticResponse
     */
    @Transactional
    @CacheEvict(value = "aquatics", allEntries = true)
    public AquaticResponse addNewAquatic(final AquaticRequest aquaticRequest) {
        log.info("Adding new aquatic");
        var aquatic = aquaticRepository.save(aquaticMapper.toAquatic(aquaticRequest));
        return aquaticMapper.toAquaticResponse(aquatic);
    }

    /**
     * Updates an existing aquatic entry and evicts the cache.
     *
     * @param id             the ID of the aquatic to update
     * @param aquaticRequest the updated aquatic data
     * @return the updated AquaticResponse
     */
    @Transactional
    @CacheEvict(value = "aquatics", allEntries = true)
    public AquaticResponse updateAquatic(final Long id, final AquaticRequest aquaticRequest) {
        log.info("Updating aquatic with ID: {}", id);
        Aquatic existingAquatic = aquaticRepository.findById(id)
                .orElseThrow(() -> new AquaticNotFoundException("Aquatic not found with id: " + id));

        Aquatic updatedAquatic = aquaticMapper.toAquatic(aquaticRequest);
        updatedAquatic.setId(existingAquatic.getId());

        Aquatic saved = aquaticRepository.save(updatedAquatic);
        return aquaticMapper.toAquaticResponse(saved);
    }

    /**
     * Deletes an aquatic entry by ID and evicts the cache.
     *
     * @param id the ID of the aquatic to delete
     */
    @Transactional
    @CacheEvict(value = "aquatics", allEntries = true)
    public void deleteAquatic(final Long id) {
        log.info("Deleting aquatic with ID: {}", id);
        if (!aquaticRepository.existsById(id)) {
            throw new AquaticNotFoundException("Aquatic not found with id: " + id);
        }
        aquaticRepository.deleteById(id);
    }
}

