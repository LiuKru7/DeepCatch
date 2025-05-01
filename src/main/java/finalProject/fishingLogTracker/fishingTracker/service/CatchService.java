package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
import finalProject.fishingLogTracker.fishingTracker.exception.CatchNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.CatchMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CatchService {

    private final CatchRepository catchRepository;
    private final CatchMapper catchMapper;

    public CatchResponse addCatch(@Valid CatchRequest catchRequest) {
        log.info("Creating new Catch : {}", catchRequest);
        Catch catchEntity = catchMapper.toCatch(catchRequest);
        Catch savedCatch = catchRepository.save(catchEntity);
        return catchMapper.toCatchResponse(savedCatch);
    }

    public CatchResponse getCatchById(Long id) {
        log.info("Fetching Catch with ID: {}", id);
        Catch catchEntity = catchRepository.findById(id)
                .orElseThrow(() -> new CatchNotFoundException("Catch not found with id: " + id));
        return catchMapper.toCatchResponse(catchEntity);
    }

    public List<CatchResponse> getAllCatches() {
        log.info("Fetching all Catch entries");
        return catchRepository.findAll()
                .stream()
                .map(catchMapper::toCatchResponse)
                .toList();
    }

    public CatchResponse updateCatch(Long id, @Valid CatchRequest catchRequest) {
        log.info("Updating Catch with ID: {}", id);
        Catch existingCatch = catchRepository.findById(id)
                .orElseThrow(() -> new CatchNotFoundException("Catch not found with id: " + id));

        Catch updatedCatch = catchMapper.toCatch(catchRequest);
        updatedCatch.setId(existingCatch.getId());
        Catch saved = catchRepository.save(updatedCatch);

        return catchMapper.toCatchResponse(saved);
    }

    public void deleteCatch(Long id) {
        log.info("Deleting Catch with ID: {}", id);

        if (!catchRepository.existsById(id)) {
            log.error("Catch not found with id: {}", id);
            throw new CatchNotFoundException("Catch not found with id: " + id);
        }

        catchRepository.deleteById(id);
    }
}
