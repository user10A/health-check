package healthcheck.api;

import healthcheck.dto.Application.request.ApplicationProcessedRequest;
import healthcheck.dto.Application.request.ApplicationRequest;
import healthcheck.dto.Application.response.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
    @Operation(summary = "Get Application", description = "Endpoint to get application (Admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ApplicationResponse> getApplication(@RequestParam String word){
        return applicationService.getApplications(word);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete by id", description = "Endpoint to delete by id (Admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse deleteById(@PathVariable Long id){
        return applicationService.deleteById(id);
    }
    @DeleteMapping("all")
    @Operation(summary = "delete All", description = "Endpoint to delete all (Admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse deleteAll(@RequestBody List<Long>listId){
        return applicationService.deleteAll(listId);
    }
    @GetMapping("getAll")
    @Operation(summary = "get all Application", description = "Endpoint to get all application (Admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ApplicationResponse> getAllApplication(){
        return applicationService.getAllApplications();
    }
    @PutMapping("update")
    @Operation(summary = "update processed ", description = "Endpoint to update processed by id (Admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean processed(@RequestBody ApplicationProcessedRequest processed){
        return applicationService.processedById(processed);
    }
}