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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaitService {

    private final BaitRepository baitRepository;
    private final BaitMapper baitMapper;


    public List<BaitResponse> getAllBaits() {
        return baitRepository.findAll().stream()
                .map(baitMapper::toBaitResponse)
                .toList();
    }

    public BaitResponse addNewBait(BaitRequest baitRequest) {
        var bait = baitRepository.save(baitMapper.toBait(baitRequest));
        return baitMapper.toBaitResponse(bait);
    }

    public BaitResponse updateBait(Long id, BaitRequest baitRequest) {
        log.info("Updating Bait with ID: {}", id);
        Bait existingBait = baitRepository.findById(id)
                .orElseThrow(() -> new BaitNotFoundException("Bait not found with id: " + id));
        Bait updatedBait = baitMapper.toBait(baitRequest);
        updatedBait.setId(existingBait.getId());
        Bait saved = baitRepository.save(updatedBait);

        return baitMapper.toBaitResponse(saved);
    }

    public void deleteBait(Long id) {
        log.info("Deleting Bait with ID: {}", id);

        if (!baitRepository.existsById(id)) {
            throw new BaitNotFoundException("Bait not found with id: " + id);
        }

        baitRepository.deleteById(id);
    }
}
