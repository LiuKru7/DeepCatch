package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
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

    public Catch addCatch(@Valid Catch catchBody) {
        log.info("Creating new Catch : {}", catchBody);
        return catchRepository.save(catchBody);
    }

    public Catch getCatchById(Long id) {
        log.info("Fetching Catch with ID: {}", id);
        return catchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catch not found with id: " + id));
    }

    public List<Catch> getAllCatches() {
        log.info("Fetching all Catch entries");
        return catchRepository.findAll();
    }

    public Catch updateCatch(Long id, @Valid Catch updatedCatch) {
        log.info("Updating Catch with ID: {}", id);
        Catch existingCatch = catchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catch not found with id: " + id));

        updatedCatch.setId(existingCatch.getId());
        return catchRepository.save(updatedCatch);
    }

    public void deleteCatch(Long id) {
        log.info("Deleting Catch with ID: {}", id);

        if (!catchRepository.existsById(id)) {
            log.error("Catch not found with id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catch not found with id: " + id);
        }

        catchRepository.deleteById(id);
    }
}
