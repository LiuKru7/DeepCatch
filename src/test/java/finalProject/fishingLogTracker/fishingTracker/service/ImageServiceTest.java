package finalProject.fishingLogTracker.fishingTracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    private ImageService imageService;

    @Mock
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(s3Service);
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

        String expectedKey = "test-key.jpg";
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn(expectedKey);

        // Act
        String result = imageService.saveFile(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(expectedKey));
    }

    @Test
    void saveFile_ShouldHandleEmptyFile() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[0]);

        String expectedKey = "empty-file-key.jpg";
        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn(expectedKey);

        // Act
        String result = imageService.saveFile(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(expectedKey));
    }

    @Test
    void saveFile_ShouldThrowException_WhenFileIsNull() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> imageService.saveFile(null));
    }
}