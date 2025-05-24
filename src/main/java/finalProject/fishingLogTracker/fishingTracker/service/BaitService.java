package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.entity.Bait;
import finalProject.fishingLogTracker.fishingTracker.exception.AquaticNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.exception.BaitNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.BaitMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaitService {

    private final BaitRepository baitRepository;
    private final BaitMapper baitMapper;

    @Cacheable("baits")
    public List<BaitResponse> getAllBaits() {
        return baitRepository.findAll().stream()
                .map(baitMapper::toBaitResponse)
                .toList();
    }

    @CacheEvict(value = "baits", allEntries = true)
    public BaitResponse addNewBait(BaitRequest baitRequest) {
        log.info("Adding new bait: {}", baitRequest);
        var bait = baitRepository.save(baitMapper.toBait(baitRequest));
        log.info("Successfully added new bait with ID: {}", bait.getId());
        return baitMapper.toBaitResponse(bait);
    }

    @CacheEvict(value = "baits", allEntries = true)
    public BaitResponse updateBait(Long id, BaitRequest baitRequest) {
        log.info("Updating Bait with ID: {}", id);
        Bait existingBait = baitRepository.findById(id)
                .orElseThrow(() -> new BaitNotFoundException("Bait not found with id: " + id));
        Bait updatedBait = baitMapper.toBait(baitRequest);
        updatedBait.setId(existingBait.getId());
        Bait saved = baitRepository.save(updatedBait);
        log.info("Successfully updated bait with ID: {}", id);
        return baitMapper.toBaitResponse(saved);
    }

    @CacheEvict(value = "baits", allEntries = true)
    public void deleteBait(Long id) {
        log.info("Deleting Bait with ID: {}", id);

        if (!baitRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent bait with ID: {}", id);
            throw new BaitNotFoundException("Bait not found with id: " + id);
        }

        baitRepository.deleteById(id);
        log.info("Successfully deleted bait with ID: {}", id);
    }
}
