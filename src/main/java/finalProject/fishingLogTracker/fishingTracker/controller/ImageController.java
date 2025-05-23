package finalProject.fishingLogTracker.fishingTracker.controller;


import finalProject.fishingLogTracker.fishingTracker.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;

    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String key = s3Service.uploadFile(file);
            return ResponseEntity.ok("Įkelta į S3 su key: " + key);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Klaida įkeliant: " + e.getMessage());
        }
    }
}
