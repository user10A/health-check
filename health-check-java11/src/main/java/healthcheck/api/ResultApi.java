package healthcheck.api;
import healthcheck.S3.S3Service;
import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result")
public class ResultApi {
    private final ResultService resultService;
    private final S3Service s3Service;

    @PostMapping("/save")
    public ResponseEntity<SimpleResponse> saveResult(@RequestBody RequestSaveResult request) {
        SimpleResponse response = resultService.saveResult(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    @Operation(summary = "Download file", description = "Download file from S3")
    @GetMapping("/download")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ByteArrayResource> download(@RequestParam(name = "fileLink") String fileLink) {
        return s3Service.download(fileLink);
    }

}
