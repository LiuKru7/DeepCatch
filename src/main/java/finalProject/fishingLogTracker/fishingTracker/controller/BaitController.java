package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.service.BaitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bait")
@RequiredArgsConstructor
@Slf4j
public class BaitController {

    private final BaitService baitService;

    @GetMapping
    public ResponseEntity<List<BaitResponse>> getAllBaits() {
        return ResponseEntity.ok(baitService.getAllBaits());
    }

    @PostMapping
    public ResponseEntity<BaitResponse> addNewBait(@Valid @RequestBody BaitRequest baitRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baitService.addNewBait(baitRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaitResponse> updateBait(
            @PathVariable Long id,
            @RequestBody BaitRequest baitRequest) {
        log.info("Received request to update Bait ID: {}", id);
        return ResponseEntity.ok(baitService.updateBait(id, baitRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBait(@PathVariable Long id) {
        log.info("Received request to delete Bait: {}", id);
        baitService.deleteBait(id);
        return ResponseEntity.noContent().build();
    }

}
