package healthcheck.api;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationApi {

    private final ApplicationService applicationService;

    @Operation(summary = "Create a new application", description = "Endpoint for creating a new application")
    @PostMapping("/applications")
    public SimpleResponse createApplication(@Valid @RequestBody ApplicationRequest applicationRequest){
        return applicationService.createApplication(applicationRequest);
    }
}