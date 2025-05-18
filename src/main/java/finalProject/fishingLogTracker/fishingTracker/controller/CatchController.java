package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import finalProject.fishingLogTracker.fishingTracker.service.CatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/catch")
@RequiredArgsConstructor
@Slf4j
public class CatchController {

    private final CatchService catchService;

    @PostMapping("/withoutPhoto")
    public ResponseEntity<CatchResponse> addCatch(@RequestBody @Valid CatchRequest catchRequest) {
        log.info("Received request to create Catch");
        CatchResponse newCatch = catchService.addCatch(catchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCatch);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CatchResponse> addCatchWithPhoto(
            @AuthenticationPrincipal User user,
            @RequestPart("catch") CatchRequest catchRequest,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(catchService.addCatchWithPhoto(catchRequest, file, user.getId()));
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
    public ResponseEntity<Void> deleteCatch(@PathVariable Long id) {
        log.info("Received request to delete Catch: {}", id);
        catchService.deleteCatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fishing_style/{style}")
    public ResponseEntity<List<CatchResponse>> getCatchesByFishingStyle(@PathVariable FishingStyle style) {
        log.info("Received request to get Catch by Fishing style: {}", style);
        return ResponseEntity.ok(catchService.getCatchesByFishingStyle(style));
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CatchResponse>> getUserCatches(final Authentication authentication) {
        log.info("Received request to get catches for authenticated user");
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(catchService.getCatchesByUser(user.getId()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<CatchResponse>> getCatchesByUserId(@PathVariable Long id) {
        log.info("Received request to get Catch by ID: {}", id);
        return ResponseEntity.ok(catchService.getCatchesByUserId(id));
    }

}
