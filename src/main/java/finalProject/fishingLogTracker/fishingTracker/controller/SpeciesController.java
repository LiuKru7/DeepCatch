package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.service.SpeciesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
@Slf4j
public class SpeciesController {

    private final SpeciesService speciesService;

    /**
     * Retrieves a list of all species.
     *
     * @return list of SpeciesResponse
     */
    @GetMapping
    public ResponseEntity<List<SpeciesResponse>> getAllSpecies() {
        log.info("Received request to get all species");
        List<SpeciesResponse> speciesList = speciesService.getAllSpecies();
        return ResponseEntity.ok(speciesList);
    }

    /**
     * Adds a new species.
     *
     * @param speciesRequest the species data to be added
     * @return the created SpeciesResponse
     */
    @PostMapping
    public ResponseEntity<SpeciesResponse> addNewSpecies(@Valid @RequestBody final SpeciesRequest speciesRequest) {
        log.info("Received request to add a new species: {}", speciesRequest);
        SpeciesResponse createdSpecies = speciesService.addNewSpecies(speciesRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpecies);
    }

    /**
     * Updates an existing species.
     *
     * @param id             the ID of the species to update
     * @param speciesRequest the updated species data
     * @return the updated SpeciesResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpeciesResponse> updateSpecies(
            @PathVariable final Long id,
            @Valid @RequestBody final SpeciesRequest speciesRequest) {
        log.info("Received request to update Species ID: {}", id);
        SpeciesResponse updatedSpecies = speciesService.updateSpecies(id, speciesRequest);
        return ResponseEntity.ok(updatedSpecies);
    }

    /**
     * Deletes a species by ID.
     *
     * @param id the ID of the species to delete
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecies(@PathVariable final Long id) {
        log.info("Received request to delete Species: {}", id);
        speciesService.deleteSpecies(id);
        return ResponseEntity.noContent().build();
    }
}
