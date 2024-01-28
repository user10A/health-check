package healthcheck.api;

import healthcheck.dto.Department.DepartmentResponse;
import healthcheck.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/department")
public class DepartmentApi {

    private  final DepartmentService departmentService;

    @PreAuthorize("hasAuthority('ADMIN,USER')")
    @GetMapping("/getAllFacility")
    @Operation( summary = "Получение услуг",description = "метод получение услуг")
    public List<DepartmentResponse>getAllFacility() {
        return departmentService.getAllFacility();
    }
}
