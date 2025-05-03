package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.service.AquaticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/aquatic")
@RequiredArgsConstructor
public class AquaticController {

    private final AquaticService aquaticService;

    @GetMapping
    private final ResponseEntity<List<AquaticResponse>> getAllAquatics() {
        return ResponseEntity.ok(aquaticService.getAllAquatics());
    }

    @PostMapping
    private final ResponseEntity<AquaticResponse> addNewAquatic(
            @RequestBody final AquaticRequest aquaticRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aquaticService.addNewAquatic(aquaticRequest));
    }

}
