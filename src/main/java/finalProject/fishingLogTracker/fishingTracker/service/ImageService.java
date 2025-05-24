package finalProject.fishingLogTracker.fishingTracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
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
        if (file == null) {
            throw new RuntimeException("File cannot be null");
        }
        try {
            String key = s3Service.uploadFile(file);
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;

        } catch (IOException e) {
            throw new RuntimeException("Nepavyko įkelti failo į S3", e);
        }
    }
}
