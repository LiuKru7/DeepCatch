package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.mapper.AquaticMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AquaticService {

    private final AquaticRepository aquaticRepository;
    private final AquaticMapper aquaticMapper;

    public List<AquaticResponse> getAllAquatics() {
        return aquaticRepository.findAll().stream()
                .map(aquaticMapper::toAquaticResponse)
                .toList();
    }

    public AquaticResponse addNewAquatic(final AquaticRequest aquaticRequest) {
        var aquatic = aquaticRepository.save(aquaticMapper.toAquatic(aquaticRequest));
        return aquaticMapper.toAquaticResponse(aquatic);
    }
}
