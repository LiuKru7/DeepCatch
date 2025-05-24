package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.exception.FileUploadException;
import finalProject.fishingLogTracker.fishingTracker.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ImageService {

    private final S3Service s3Service;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public ImageService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String saveFile(MultipartFile file) {
        log.info("Attempting to save file: {}", file.getOriginalFilename());
        if (file == null) {
            log.error("File upload failed: File is null");
            throw new InvalidFileException("File cannot be null");
        }
        try {
            String key = s3Service.uploadFile(file);
            String url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
            log.info("File successfully uploaded to S3: {}", url);
            return url;

        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new FileUploadException("Nepavyko įkelti failo į S3", e);
        }
    }
}
