package healthcheck.api;

import healthcheck.dto.Application.ApplicationDelete;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/application")
@CrossOrigin
@Tag(name = "Application api", description = "API's for applications ")
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

    @DeleteMapping
    @Operation(summary = "delete is processed", description = "Endpoint to delete is processed")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public SimpleResponse deleteProcessed(@RequestBody ApplicationDelete application){
        return applicationService.deleteAllPressed(application);
    }
    @DeleteMapping("all")
    @Operation(summary = "delete All", description = "Endpoint to delete all")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public SimpleResponse deleteAll(@RequestBody List<ApplicationDelete>application){
        return applicationService.deleteAll(application);
    }

    @GetMapping("getAll")
    @Operation(summary = "get all Application", description = "Endpoint to get all application")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<ApplicationResponse> getAllApplication(){
        return applicationService.getAllApplications();
    }
}