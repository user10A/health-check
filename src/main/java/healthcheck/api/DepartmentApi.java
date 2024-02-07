package healthcheck.api;

import healthcheck.dto.Department.DepartmentResponse;
import healthcheck.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/department")
@CrossOrigin
@Tag(name = "Department api", description = "API's for department ")
public class DepartmentApi {

    private  final DepartmentService departmentService;

    @GetMapping("/getAllFacility")
    @Operation(summary = "Get All Facilities", description = "Retrieve a list of facilities. This method fetches information about available facilities.")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<DepartmentResponse> getAllFacility() {
        return departmentService.getAllFacility();
    }
}