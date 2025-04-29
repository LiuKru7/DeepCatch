package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.service.SpeciesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
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
}
