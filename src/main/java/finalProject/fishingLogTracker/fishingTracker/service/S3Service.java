package finalProject.fishingLogTracker.fishingTracker.service;

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

@Service
public class S3Service {

    private final S3Client s3;
    private final String bucketName;

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
    }

    public String uploadFile(MultipartFile file) throws IOException {
        MultipartFile fileToUpload = file;

        // Tik konvertuoti, jei tai ne JPEG arba jei failas labai didelis (pvz., > 2MB)
        if (!"image/jpeg".equalsIgnoreCase(file.getContentType()) || file.getSize() > 2_000_000) {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new IllegalArgumentException("Nepavyko perskaityti paveikslėlio – nepalaikomas formatas?");
            }

            // Paverčiam į RGB (pašalinam skaidrumą jei yra)
            BufferedImage rgbImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g = rgbImage.createGraphics();
            g.drawImage(originalImage, 0, 0, Color.WHITE, null);
            g.dispose();

            // Paruošiam JPEG encoderį su kokybe
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) {
                throw new IllegalStateException("JPEG encoder nerastas");
            }
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.85f); // 85% kokybė
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(rgbImage, null, null), param);
            }
            writer.dispose();

            // Sukuriam naują MultipartFile iš konvertuoto JPEG
            fileToUpload = new MockMultipartFile(
                    "file",
                    file.getOriginalFilename().replaceAll("\\.[^.]+$", "") + ".jpg",
                    "image/jpeg",
                    new ByteArrayInputStream(os.toByteArray())
            );
        }

        // Generuojam unikalų S3 key
        String key = UUID.randomUUID() + "_" + fileToUpload.getOriginalFilename();

        // Įkėlimas į S3
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(fileToUpload.getContentType())
                        .serverSideEncryption(ServerSideEncryption.AES256)
                        .build(),
                RequestBody.fromBytes(fileToUpload.getBytes())
        );

        return key;
    }
}
