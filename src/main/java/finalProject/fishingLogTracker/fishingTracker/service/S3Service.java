package finalProject.fishingLogTracker.fishingTracker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.UUID;

@Slf4j
@Service
public class S3Service {
    private final S3Client s3;
    private final String bucketName;

    /**
     * Constructs the S3Service with AWS credentials and bucket info.
     *
     * @param accessKey  AWS access key
     * @param secretKey  AWS secret key
     * @param region     AWS region
     * @param bucketName S3 bucket name
     */
    public S3Service(
            @Value("${aws.accessKey}") String accessKey,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.region}") String region,
            @Value("${aws.bucketName}") String bucketName) {

        this.bucketName = bucketName;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        log.info("S3 client initialized for bucket: {}", bucketName);
    }

    /**
     * Uploads a file to the configured S3 bucket.
     * If the file is not JPEG or larger than 2MB, it will be converted to JPEG with 85% quality.
     *
     * @param file Multipart file to upload
     * @return the unique key of the uploaded file in S3
     * @throws IOException              if reading or writing the image fails
     * @throws IllegalArgumentException if the file cannot be read as an image
     * @throws IllegalStateException    if JPEG encoder is not found
     */
    public String uploadFile(MultipartFile file) throws IOException {
        MultipartFile fileToUpload = file;

        if (!"image/jpeg".equalsIgnoreCase(file.getContentType()) || file.getSize() > 2_000_000) {
            log.debug("Converting file {} to JPEG due to content type or size", file.getOriginalFilename());

            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                log.error("Failed to read image file: {}", file.getOriginalFilename());
                throw new IllegalArgumentException("Unable to read image file - unsupported format?");
            }

            BufferedImage rgbImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g = rgbImage.createGraphics();
            g.drawImage(originalImage, 0, 0, Color.WHITE, null);
            g.dispose();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) {
                log.error("JPEG encoder not found");
                throw new IllegalStateException("JPEG encoder not found");
            }
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.85f);
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(rgbImage, null, null), param);
            }
            writer.dispose();

            fileToUpload = new MockMultipartFile(
                    "file",
                    file.getOriginalFilename().replaceAll("\\.[^.]+$", "") + ".jpg",
                    "image/jpeg",
                    new ByteArrayInputStream(os.toByteArray())
            );

            log.debug("File converted to JPEG: {}", fileToUpload.getOriginalFilename());
        }

        String key = UUID.randomUUID() + "_" + fileToUpload.getOriginalFilename();

        log.info("Uploading file {} to S3 bucket {} with key {}", fileToUpload.getOriginalFilename(), bucketName, key);

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(fileToUpload.getContentType())
                        .serverSideEncryption(ServerSideEncryption.AES256)
                        .build(),
                RequestBody.fromBytes(fileToUpload.getBytes())
        );

        log.info("File uploaded successfully with key: {}", key);

        return key;
    }
}