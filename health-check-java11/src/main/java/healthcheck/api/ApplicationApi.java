package healthcheck.api;

import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/application")
public class ApplicationApi {

    private final ApplicationService applicationService;

    @PostMapping("/createApplication")
    @Operation(summary = "Create Application", description = "Endpoint to create a new application.")
    public SimpleResponse createApplication(@Valid @RequestBody ApplicationRequest applicationRequest){
        return applicationService.createApplication(applicationRequest);
    }

    @GetMapping("/getApplication")
    @Operation(summary = "Get Application", description = "Endpoint to get application.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ApplicationResponse> getApplication(@RequestParam String word){
        return applicationService.getApplications(word);
    }
}