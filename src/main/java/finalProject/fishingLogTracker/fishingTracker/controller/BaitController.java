package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.service.BaitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bait")
@RequiredArgsConstructor
public class BaitController {

    private final BaitService baitService;

    @GetMapping
    public ResponseEntity<List<BaitResponse>> getAllBaits() {
        return ResponseEntity.ok(baitService.getAllBaits());
    }

    @PostMapping
    public ResponseEntity<BaitResponse> addNewBait(@RequestBody BaitRequest baitRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baitService.addNewBait(baitRequest));
    }
}
