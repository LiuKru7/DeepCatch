package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.mapper.BaitMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
}
