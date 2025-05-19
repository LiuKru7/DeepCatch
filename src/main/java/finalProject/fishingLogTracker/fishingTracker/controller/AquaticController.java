package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.service.AquaticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/aquatic")
@RequiredArgsConstructor
@Slf4j
public class AquaticController {

    private final AquaticService aquaticService;


    /**
     * Retrieves a list of all aquatic entities.
     *
     * @return ResponseEntity containing a list of AquaticResponse
     */
    @GetMapping
    public ResponseEntity<List<AquaticResponse>> getAllAquatics() {
        log.info("Fetching all aquatics");
        return ResponseEntity.ok(aquaticService.getAllAquatics());
    }

    /**
     * Adds a new aquatic entity.
     *
     * @param aquaticRequest the details of the aquatic to be added
     * @return ResponseEntity containing the created AquaticResponse
     */
    @PostMapping
    public ResponseEntity<AquaticResponse> addNewAquatic(
            @Valid @RequestBody final AquaticRequest aquaticRequest) {
        log.info("Adding new aquatic: {}", aquaticRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aquaticService.addNewAquatic(aquaticRequest));
    }

    /**
     * Updates an existing aquatic entity.
     *
     * @param id ID of the aquatic to update
     * @param aquaticRequest updated details of the aquatic
     * @return ResponseEntity containing the updated AquaticResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<AquaticResponse> updateAquatic(
            @PathVariable final Long id,
            @RequestBody final AquaticRequest aquaticRequest) {
        log.info("Received request to update Aquatic ID: {}", id);
        return ResponseEntity.ok(aquaticService.updateAquatic(id, aquaticRequest));
    }

    /**
     * Deletes an aquatic entity by ID.
     *
     * @param id ID of the aquatic to delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAquatic(@PathVariable final Long id) {
        log.info("Received request to delete Aquatic: {}", id);
        aquaticService.deleteAquatic(id);
        return ResponseEntity.noContent().build();
    }
}
