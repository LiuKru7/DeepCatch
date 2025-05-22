package finalProject.fishingLogTracker.fishingTracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    private ImageService imageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        imageService = new ImageService();
    }

    @Test
    void saveFile_ShouldSaveFileAndReturnFileName() throws IOException {
        // Arrange
        String originalFilename = "test.jpg";
        byte[] content = "test image content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "file",
                originalFilename,
                "image/jpeg",
                content);

        // Act
        String result = imageService.saveFile(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.endsWith(originalFilename));
        assertTrue(Files.exists(Path.of("uploads", result)));
    }

    @Test
    void saveFile_ShouldHandleEmptyFile() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[0]);

        // Act
        String result = imageService.saveFile(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.endsWith("test.jpg"));
        assertTrue(Files.exists(Path.of("uploads", result)));
    }

    @Test
    void saveFile_ShouldThrowException_WhenFileIsNull() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> imageService.saveFile(null));
    }
}