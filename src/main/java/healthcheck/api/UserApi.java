package healthcheck.api;

import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.*;
import healthcheck.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin
@Tag(name = "User api", description = "API's for user ")
public class UserApi {
    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/getAllAppointmentsOfUser")
    @Operation(summary = "get all appointments of user",
            description = "this method allows to get all appointments")
    public List<ResponseToGetUserAppointments> getAllUsersAppointments() {
        return userService.getAllAppointmentsOfUser();
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/{id}/userAppointment")
    @Operation(summary = "get appointment by user id",
            description = "page of appointment of user")
    public ResponseToGetAppointmentByUserId getUserAppointmentById(@PathVariable Long id) {
        return userService.getUserAppointmentById(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/deleteAppointments")
    @Operation(summary = "delete appointments of user",
            description = " this method allows to delete appointments of user")
    public String clearUsersAppointments() {
        int deletedCount = userService.clearMyAppointments();
        return "Deleted " + deletedCount + " appointments.";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/editUserProfile")
    @Operation(summary = "Edit profile by user",
            description = "Edit")
    public SimpleResponse editUserProfile(@Valid @RequestBody ProfileRequest profileRequest){
        return userService.editUserProfile(profileRequest);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/getResponse")
    @Operation(summary = "Response information user by token",
            description = "Information")
    public GetUserResponseByToken responseUserProfile(){
        return userService.responseUserInfo();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/changeUserPassword")
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
    @GetMapping("/search")
    @Operation(summary = "Search Patients", description = "Method to search for patients by name, full name, or email.")
    public List<ResultUsersResponse> getPatientsBySearch(@RequestParam String word){
        return userService.getAllPatientsBySearch(word);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "get user by id")
    public ResponseToGetUserById getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}