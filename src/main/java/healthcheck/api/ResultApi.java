package healthcheck.api;

import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result")
public class ResultApi {

    private final ResultService resultService;

    @PostMapping("/save")
    @Operation(summary = "Save Result", description = "This API is used to save a result.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SimpleResponse> saveResult(@RequestBody RequestSaveResult request) {
        SimpleResponse response = resultService.saveResult(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}