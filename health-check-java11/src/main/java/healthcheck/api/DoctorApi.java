package healthcheck.api;

import healthcheck.dto.Doctor.DoctorResponse;
import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.Doctor.DoctorUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorApi {

    private final DoctorService doctorService;

    @PostMapping("/saveDoctor")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Save a new doctor", description = "This endpoint allows an admin to save a new doctor.")
    public ResponseEntity<SimpleResponse> saveDoctor(@RequestBody DoctorSaveRequest request) {
        return ResponseEntity.ok(doctorService.saveDoctor(request));
    }
    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get Doctor", description = "This endpoint allows an admin to get doctor.")
    public DoctorResponse getDoctorById(@RequestParam Long id) {
        return doctorService.getDoctorById(id);
    }
    @PatchMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update doctor", description = "This endpoint allows an admin to update doctor.")
    public SimpleResponse updateDoctor(@RequestParam Long id,@RequestBody DoctorUpdateRequest request) {
        return doctorService.updateDoctor(id,request);
    }
}
