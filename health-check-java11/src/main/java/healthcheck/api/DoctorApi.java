package healthcheck.api;

import healthcheck.dto.Doctor.DoctorResponse;
import healthcheck.dto.Doctor.DoctorResponseByWord;
import healthcheck.dto.Doctor.DoctorSaveRequest;
import healthcheck.dto.Doctor.DoctorUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Doctor;
import healthcheck.enums.Facility;
import healthcheck.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorApi {

    private final DoctorService doctorService;

    @PostMapping()
    @Operation(summary = "Save a new doctor", description = "This endpoint allows an admin to save a new doctor.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SimpleResponse> saveDoctor(@RequestBody DoctorSaveRequest request) {
        return ResponseEntity.ok(doctorService.saveDoctor(request));
    }

    @GetMapping()
    @Operation(summary = "Get Doctor", description = "This endpoint allows an admin to get doctor.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DoctorResponse getDoctorById(@RequestParam Long id) {
        return doctorService.getDoctorById(id);
    }

    @PatchMapping()
    @Operation(summary = "Update doctor", description = "This endpoint allows an admin to update doctor.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse updateDoctor(@RequestParam Facility facility, @RequestParam Long id,@RequestBody DoctorUpdateRequest request) {
        return doctorService.updateDoctor(facility, id,request);
    }

    @GetMapping("/byDepartment")
    @Operation(summary = "Get doctors by department", description = "This API is used to retrieve a list of doctors based on the specified facility")
    public List<Doctor> getDoctorsByDepartment(@RequestParam Facility facility) {
        return doctorService.getDoctorsByDepartment(facility);
    }

    // Специалисты. Методы: 1 - Search по именам. 2 - Вывод всех докторов. 3 - Удаление ->
    @GetMapping("/doctors/search")
    @Operation(summary = "Get doctors by search", description = "This API is used to retrieve a list of doctors based on the specified search term")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DoctorResponseByWord> getAllDoctorsBySearch(@RequestParam String word) {
        return doctorService.getAllDoctorsBySearch(word);
    }

    @GetMapping("/getAllDoctors")
    @Operation(summary = "Get all doctors", description = "This API is used to retrieve a list of all doctors")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DoctorResponseByWord> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @DeleteMapping("/{doctorId}")
    @Operation(summary = "Delete doctor by ID", description = "This API is used to delete a doctor by ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse deleteDoctorById(@PathVariable Long doctorId) {
        return doctorService.deleteDoctorById(doctorId);
    }
}
