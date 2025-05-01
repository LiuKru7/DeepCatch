package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Catch;
import finalProject.fishingLogTracker.fishingTracker.service.CatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/catch")
@RequiredArgsConstructor
@Slf4j
public class CatchController {

    private final CatchService catchService;

    @PostMapping
    public ResponseEntity<CatchResponse> addCatch(@RequestBody @Valid CatchRequest catchRequest) throws IOException {
        log.info("Received request to create Catch");
        CatchResponse newCatch = catchService.addCatch(catchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCatch);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatchResponse> getCatchById(@PathVariable Long id) {
        log.info("Received request to get Catch by ID: {}", id);
        return ResponseEntity.ok(catchService.getCatchById(id));
    }

    @GetMapping
    public ResponseEntity<List<CatchResponse>> getAllCatches() {
        log.info("Received request to get all Catches");
        return ResponseEntity.ok(catchService.getAllCatches());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatchResponse> updateCatch(
            @PathVariable Long id,
            @Valid @RequestBody CatchRequest catchRequest) {
        log.info("Received request to update Catch ID: {}", id);
        return ResponseEntity.ok(catchService.updateCatch(id, catchRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCatch(@PathVariable Long id) {
        log.info("Received request to delete Catch: {}", id);
        catchService.deleteCatch(id);
        return ResponseEntity.noContent().build();
    }

}
