package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.service.AquaticService;
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

    @GetMapping
    public ResponseEntity<List<AquaticResponse>> getAllAquatics() {
        return ResponseEntity.ok(aquaticService.getAllAquatics());
    }

    @PostMapping
    public ResponseEntity<AquaticResponse> addNewAquatic(
            @RequestBody final AquaticRequest aquaticRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aquaticService.addNewAquatic(aquaticRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AquaticResponse> updateAquatic (
            @PathVariable Long id,
            @RequestBody AquaticRequest aquaticRequest) {
        log.info("Received request to update Aquatic ID: {}", id);
        return ResponseEntity.ok(aquaticService.updateAquatic(id, aquaticRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAquatic(@PathVariable Long id) {
        log.info("Received request to delete Aquatic: {}", id);
        aquaticService.deleteAquatic(id);
        return ResponseEntity.noContent().build();
    }

}
