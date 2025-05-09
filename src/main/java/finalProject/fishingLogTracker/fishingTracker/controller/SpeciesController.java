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

    @GetMapping
    public ResponseEntity<List<SpeciesResponse>> getAllSpecies() {
        return ResponseEntity.ok(speciesService.getAllSpecies());
    }

    @PostMapping
    public ResponseEntity<SpeciesResponse> addNewSpecies(@RequestBody SpeciesRequest speciesRequest) {
        return ResponseEntity.ok(speciesService.addNewSpecies(speciesRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpeciesResponse> updateSpecies(
            @PathVariable Long id,
            @RequestBody SpeciesRequest speciesRequest) {
        log.info("Received request to update Species ID: {}", id);
        return ResponseEntity.ok(speciesService.updateSpecies(id, speciesRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpecies(@PathVariable long id) {
        log.info("Received request to delete Species: {}", id);
        speciesService.deleteSpecies(id);
        return ResponseEntity.noContent().build();
    }
}
