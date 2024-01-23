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
    @Operation(summary = "get all appointments of user",
            description = "this method allows to get all appointments")
    public List<ResponseToGetUserAppointments> getAllUsersAppointments() {
        return userService.getAllAppointmentsOfUser();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get appointment by user id",
            description = "page of appointment of user")
    public ResponseToGetAppointmentByUserId getUserAppointmentById(@PathVariable Long id) {
        return userService.getUserAppointmentById(id);
    }

    @DeleteMapping("/deleteAppointments")
    @Operation(summary = "delete appointments of user",
            description = " this method allows to delete appointments of user")
    public String clearUsersAppointments() {
        int deletedCount = userService.clearMyAppointments();
        return "Deleted " + deletedCount + " appointments.";
    }

    @PostAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public SimpleResponse editUserProfile(@Valid @RequestBody ProfileRequest profileRequest){
        return userService.editUserProfile(profileRequest);
    }

    @Operation(summary = "Change user password", description = "Endpoint for authenticated users to change their password.")
    @PostMapping("/changeUserPassword")
    @PostAuthorize("hasAuthority('USER')")
    public SimpleResponse changeUserPassword(@Valid @RequestBody ChangePasswordUserRequest changePasswordUserRequest){
        return userService.changePassword(changePasswordUserRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping()
    @Operation(summary = "Удаление пациента",description = "Метод Удаление пациента по ID")
    public SimpleResponse deletePatientById(@RequestParam Long id){
        return userService.deletePatientsById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllPatients")
    @Operation(summary = "Страница получение пациентов", description = "Метод получение всех пациентов")
    public List<ResultUsersResponse> getAllPatients(){
        return userService.getAllPatients();
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("search")
    @Operation(summary = "Страница получение пациентов по поиску",description = "метод поиска по имени и фио или по email")
    public List<ResultUsersResponse> getPatientsBySearch(@RequestParam String word){
        return userService.getAllPatientsBySearch(word);
    }

}

