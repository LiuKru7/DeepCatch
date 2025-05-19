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

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/catch")
@RequiredArgsConstructor
@Slf4j
public class CatchController {

    private final CatchService catchService;

    /**
     * Creates a new catch entry with an attached photo.
     *
     * @param user         the authenticated user who submits the catch
     * @param catchRequest the data of the catch
     * @param file         the photo of the catch
     * @return the created CatchResponse with HTTP 201 status
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CatchResponse> addCatchWithPhoto(
            @AuthenticationPrincipal final User user,
            @RequestPart("catch") final CatchRequest catchRequest,
            @RequestPart("file") final MultipartFile file) {

        log.info("Received request to add catch with photo for user ID: {}", user.getId());

        CatchResponse createdCatch = catchService.addCatchWithPhoto(catchRequest, file, user.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCatch);
    }

    /**
     * Retrieves a catch by its ID.
     *
     * @param id the ID of the catch
     * @return the CatchResponse if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatchResponse> getCatchById(@PathVariable final Long id) {

        log.info("Received request to get Catch by ID: {}", id);

        CatchResponse catchResponse = catchService.getCatchById(id);

        return ResponseEntity.ok(catchResponse);
    }

    /**
     * Retrieves all catches.
     *
     * @return list of all CatchResponse entries
     */
    @GetMapping
    public ResponseEntity<List<CatchResponse>> getAllCatches() {

        log.info("Received request to get all Catches");

        List<CatchResponse> catchResponses = catchService.getAllCatches();

        return ResponseEntity.ok(catchResponses);
    }

    /**
     * Updates an existing catch entry.
     *
     * @param id           the ID of the catch to update
     * @param catchRequest the updated catch data
     * @return the updated CatchResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<CatchResponse> updateCatch(
            @PathVariable final Long id,
            @Valid @RequestBody final CatchRequest catchRequest) {

        log.info("Received request to update Catch ID: {}", id);

        CatchResponse catchResponse = catchService.updateCatch(id, catchRequest);

        return ResponseEntity.ok(catchResponse);
    }

    /**
     * Deletes a catch by its ID.
     *
     * @param id the ID of the catch to delete
     * @return HTTP 204 No Content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatch(@PathVariable final Long id) {
        log.info("Received request to delete Catch: {}", id);
        catchService.deleteCatch(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves catches filtered by fishing style.
     *
     * @param style the fishing style to filter by
     * @return list of catches matching the given style
     */
    @GetMapping("/fishing_style/{style}")
    public ResponseEntity<List<CatchResponse>> getCatchesByFishingStyle(@PathVariable final FishingStyle style) {

        log.info("Received request to get Catch by Fishing style: {}", style);

        List<CatchResponse> catchResponses = catchService.getCatchesByFishingStyle(style);

        return ResponseEntity.ok(catchResponses);
    }

    /**
     * Retrieves catches submitted by the authenticated user.
     *
     * @param user the currently authenticated user
     * @return list of the user's own catches
     */
    @GetMapping("/user")
    public ResponseEntity<List<CatchResponse>> getUserCatches(@AuthenticationPrincipal final User user) {

        log.info("Received request to get catches for authenticated user");

        List<CatchResponse> catchResponses = catchService.getCatchesByUser(user.getId());

        return ResponseEntity.ok(catchResponses);
    }

    /**
     * Retrieves catches by a specific user ID.
     *
     * @param id the user ID
     * @return list of catches submitted by the given user
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<CatchResponse>> getCatchesByUserId(@PathVariable final Long id) {

        log.info("Received request to get Catch by ID: {}", id);

        List<CatchResponse> catchResponses = catchService.getCatchesByUserId(id);

        return ResponseEntity.ok(catchResponses);
    }
}
