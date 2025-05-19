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

    /**
     * Retrieves a list of all baits.
     *
     * @return list of BaitResponse objects
     */
    @GetMapping
    public ResponseEntity<List<BaitResponse>> getAllBaits() {
        log.info("Received request to get all Baits");
        return ResponseEntity.ok(baitService.getAllBaits());
    }

    /**
     * Adds a new bait.
     *
     * @param baitRequest the bait data to create
     * @return the created BaitResponse
     */
    @PostMapping
    public ResponseEntity<BaitResponse> addNewBait(@Valid @RequestBody final BaitRequest baitRequest) {
        log.info("Received request to create new Bait: {}", baitRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baitService.addNewBait(baitRequest));
    }

    /**
     * Updates an existing bait by ID.
     *
     * @param id ID of the bait to update
     * @param baitRequest the updated bait data
     * @return the updated BaitResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaitResponse> updateBait(
            @PathVariable final Long id,
            @RequestBody @Valid final BaitRequest baitRequest) {
        log.info("Received request to update Bait ID: {}", id);
        return ResponseEntity.ok(baitService.updateBait(id, baitRequest));
    }

    /**
     * Deletes a bait by ID.
     *
     * @param id ID of the bait to delete
     * @return HTTP 204 No Content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBait(@PathVariable final Long id) {
        log.info("Received request to delete Bait ID: {}", id);
        baitService.deleteBait(id);
        return ResponseEntity.noContent().build();
    }
}
