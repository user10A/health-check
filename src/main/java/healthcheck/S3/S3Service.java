package healthcheck.S3;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;
import java.util.Map;

@Service
@Getter
@Setter
@Slf4j
public class S3Service {
    private final S3Client s3;

    @Value("${aws.bucket.name}")
    private String BUCKET_NAME;

    @Value("${aws.bucket.path}")
    private String BUCKET_PATH;

    @Autowired
    public S3Service(S3Client s3) {
        this.s3 = s3;
    }


    public Map<String, String> upload(MultipartFile file) throws IOException {
        log.info("Uploading file ...");
        String key = System.currentTimeMillis() + file.getOriginalFilename();
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .contentType("png")
                .contentType("pdf")
                .contentLength(file.getSize())
                .key(key)
                .build();
        s3.putObject(put, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        log.info("Upload successfully deleted!");
        return Map.of(
                "link", BUCKET_PATH + key);
    }

    public Map<String, String> delete(String fileLink) {
        log.info("Deleting file...");
        try {
            String key = fileLink.substring(BUCKET_PATH.length());
            log.warn("Deleting object: {}", key);

            s3.deleteObject(dor -> dor.bucket(BUCKET_NAME).key(key).build());
        } catch (S3Exception e) {
            throw new IllegalStateException(e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return Map.of(
                "message", fileLink + " has been deleted");
    }

    public ResponseEntity<ByteArrayResource> download(String fileLink) {
        try {
            log.info("Downloading file...");
            String key = fileLink.substring(BUCKET_PATH.length());
            ResponseInputStream<GetObjectResponse> s3Object = s3.getObject(GetObjectRequest.builder().bucket(BUCKET_NAME).key(key).build());
            byte[] fileContent = IOUtils.toByteArray(s3Object);
            ByteArrayResource resource = new ByteArrayResource(fileContent);

            // Determine the content type
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (key.toLowerCase().endsWith(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key);
            headers.setContentLength(resource.contentLength());

            log.info("Download successfully completed!");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(resource);
        } catch (S3Exception | IOException e) {
            log.error("Error downloading file: {}", e.getMessage());
            throw new IllegalStateException("Error downloading file");
        }
    }
    public byte[] getPdfFileByUrl(String fileUrl) {
        try {
            log.info("Поиск file...");
            String key = fileUrl.substring(BUCKET_PATH.length());
            ResponseInputStream<GetObjectResponse> s3Object = s3.getObject(GetObjectRequest.builder().bucket(BUCKET_NAME).key(key).build());
            byte[] content = IOUtils.toByteArray(s3Object);
            log.info("File успешно найден !");
            return content;
        } catch (S3Exception | IOException e) {
            log.error("Ошибка file не найден: {}", e.getMessage());
            throw new IllegalStateException("File не найден ! ");
        }
    }
}