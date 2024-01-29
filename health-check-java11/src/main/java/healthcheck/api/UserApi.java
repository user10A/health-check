package healthcheck.api;

import healthcheck.dto.User.*;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApi {

    private final UserService userService;

    @GetMapping("/getAllAppointmentsOfUser")
    @PostAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "get all appointments of user",
            description = "this method allows to get all appointments")
    public List<ResponseToGetUserAppointments> getAllUsersAppointments() {
        return userService.getAllAppointmentsOfUser();
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "get appointment by user id",
            description = "page of appointment of user")
    public ResponseToGetAppointmentByUserId getUserAppointmentById(@PathVariable Long id) {
        return userService.getUserAppointmentById(id);
    }

    @DeleteMapping("/deleteAppointments")
    @PostAuthorize("hasAuthority('USER')")
    @Operation(summary = "delete appointments of user",
            description = " this method allows to delete appointments of user")
    public String clearUsersAppointments() {
        int deletedCount = userService.clearMyAppointments();
        return "Deleted " + deletedCount + " appointments.";
    }

    @PostAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/editUserProfile")
    @Operation(summary = "Edit profile by user",
            description = "Edit")
    public SimpleResponse editUserProfile(@Valid @RequestBody ProfileRequest profileRequest){
        return userService.editUserProfile(profileRequest);
    }

    @PostMapping("/changeUserPassword")
    @PostAuthorize("hasAuthority('USER')")
    @Operation(summary = "Change user password", description = "Endpoint for authenticated  users to change their password.")
    public SimpleResponse changeUserPassword(@Valid @RequestBody ChangePasswordUserRequest changePasswordUserRequest){
        return userService.changePassword(changePasswordUserRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patient", description = "Method to delete a patient by ID.")
    public SimpleResponse deletePatientById(@PathVariable Long id){
        return userService.deletePatientsById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllPatients")
    @Operation(summary = "Get All Patients", description = "Method to retrieve all patients.")
    public List<ResultUsersResponse> getAllPatients(){
        return userService.getAllPatients();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("search")
    @Operation(summary = "Search Patients", description = "Method to search for patients by name, full name, or email.")
    public List<ResultUsersResponse> getPatientsBySearch(@RequestParam String word){
        return userService.getAllPatientsBySearch(word);
    }
}