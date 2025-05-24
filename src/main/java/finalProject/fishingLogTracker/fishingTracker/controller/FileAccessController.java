package finalProject.fishingLogTracker.fishingTracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
public class FileAccessController {

    /**
     * Retrieves a file from the 'uploads' directory by filename.
     * <p>
     * The method checks if the file exists, detects its content type,
     * and returns it as a downloadable resource.
     * If the file is not found or an error occurs, it returns an appropriate HTTP
     * status.
     *
     * @param filename the name of the file to retrieve
     * @return ResponseEntity containing the file as a Resource, or an error status
     */
    @GetMapping("/api/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable final String filename) {
        log.info("Received request to download file: {}", filename);

        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                log.warn("File not found: {}", filename);
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            MediaType mediaType = (contentType != null)
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.APPLICATION_OCTET_STREAM;

            if (mediaType.getType().equals("image")) {
                try {
                    BufferedImage image = ImageIO.read(resource.getInputStream());
                    if (image == null) {
                        log.error("Invalid image file: {}", filename);
                        return ResponseEntity.internalServerError().build();
                    }
                } catch (IOException e) {
                    log.error("Error validating image file: {}", filename, e);
                    return ResponseEntity.internalServerError().build();
                }
            }

            log.info("Successfully found file: {}, content type: {}", filename, mediaType);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (IOException e) {
            log.error("Error while reading file: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
