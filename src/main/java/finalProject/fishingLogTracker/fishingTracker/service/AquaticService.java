package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.exception.AquaticNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.AquaticMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
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

    @Cacheable("aquatics")
    public List<AquaticResponse> getAllAquatics() {
        return aquaticRepository.findAll().stream()
                .map(aquaticMapper::toAquaticResponse)
                .toList();
    }

    @CacheEvict(value = "aquatics", allEntries = true)
    public AquaticResponse addNewAquatic(final AquaticRequest aquaticRequest) {
        var aquatic = aquaticRepository.save(aquaticMapper.toAquatic(aquaticRequest));
        return aquaticMapper.toAquaticResponse(aquatic);
    }

    @CacheEvict(value = "aquatics", allEntries = true)
    public AquaticResponse updateAquatic(final Long id, final AquaticRequest aquaticRequest) {
        log.info("Updating Aquatic with ID: {}", id);
        Aquatic existingAquatic = aquaticRepository.findById(id)
                .orElseThrow(() -> new AquaticNotFoundException("Aquatic not found with id: " + id));

        Aquatic updatedAquatic = aquaticMapper.toAquatic(aquaticRequest);
        updatedAquatic.setId(existingAquatic.getId());
        Aquatic saved = aquaticRepository.save(updatedAquatic);

        return aquaticMapper.toAquaticResponse(saved);
    }

    @CacheEvict(value = "aquatics", allEntries = true)
    public void deleteAquatic(final Long id) {
        log.info("Deleting Aquatic with ID: {}", id);

        if (!aquaticRepository.existsById(id)) {
            throw new AquaticNotFoundException("Aquatic not found with id: " + id);
        }

        aquaticRepository.deleteById(id);
    }
}


