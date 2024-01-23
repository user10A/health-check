package healthcheck.api;
import healthcheck.dto.User.UserResponse;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ProfileResponse;
import healthcheck.dto.User.UserResponseGetById;
import healthcheck.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserApi {

    private final UserService userService;

    @Operation(summary = "Edit user profile", description = "Endpoint to edit the user's profile information")
    @PostMapping("/editUserProfile")
    @PostAuthorize("hasAuthority('USER')")
    public ProfileResponse editUsersProfile(@RequestBody ProfileRequest profileRequest){
        return userService.editUserProfile(profileRequest);
    }

    @GetMapping
    @Operation(summary = "get all appointments of user",
            description = "this method allows to get all appointments")
    public List<UserResponse> getAllUsersAppointments() {
        return userService.getAllAppointmentsOfUser();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get appointment by user id",
            description = "page of appointment of user")
    public UserResponseGetById getAppointmentById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @DeleteMapping
    @Operation(summary = "delete appointments of user",
            description = " this method allows to delete appointments of user")
    public String clearUsersAppointments() {
        int deletedCount = userService.clearMyAppointments();
        return "Deleted " + deletedCount + " appointments.";
    }
}
