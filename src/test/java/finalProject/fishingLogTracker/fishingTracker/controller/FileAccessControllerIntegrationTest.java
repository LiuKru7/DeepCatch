package finalProject.fishingLogTracker.fishingTracker.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FileAccessControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String UPLOADS_DIR = "uploads";
    private static final String TEST_FILE_NAME = "test.txt";
    private static final String TEST_FILE_CONTENT = "Hello, this is a test file!";

    @BeforeEach
    void setUp() throws IOException {
        // Create uploads directory if it doesn't exist
        Path uploadsPath = Paths.get(UPLOADS_DIR);
        if (!Files.exists(uploadsPath)) {
            Files.createDirectories(uploadsPath);
        }

        // Create test file
        Path testFilePath = Paths.get(UPLOADS_DIR, TEST_FILE_NAME);
        Files.write(testFilePath, TEST_FILE_CONTENT.getBytes());
    }

    @AfterEach
    void cleanup() throws IOException {
        // Wait a bit to ensure file is not in use
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Clean up test files
        Path testFilePath = Paths.get(UPLOADS_DIR, TEST_FILE_NAME);
        if (Files.exists(testFilePath)) {
            try {
                Files.delete(testFilePath);
            } catch (IOException e) {
                // If file is still in use, try to delete it on JVM exit
                testFilePath.toFile().deleteOnExit();
            }
        }

        // Remove uploads directory if empty
        Path uploadsPath = Paths.get(UPLOADS_DIR);
        if (Files.exists(uploadsPath) && Files.isDirectory(uploadsPath)) {
            try (var files = Files.list(uploadsPath)) {
                if (files.findFirst().isEmpty()) {
                    Files.delete(uploadsPath);
                }
            }
        }
    }

    @Test
    void getFile_shouldReturnFile_whenFileExists() throws Exception {
        // Ensure file exists and is readable
        Path filePath = Paths.get(UPLOADS_DIR, TEST_FILE_NAME);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new IllegalStateException("Test file does not exist or is not readable");
        }

        mockMvc.perform(get("/api/uploads/{filename}", TEST_FILE_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    void getFile_shouldReturnNotFound_whenFileDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/uploads/{filename}", "non-existent-file.txt"))
                .andExpect(status().isNotFound());
    }
}