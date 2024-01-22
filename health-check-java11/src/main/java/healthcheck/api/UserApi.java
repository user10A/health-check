package healthcheck.api;

import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.ChangePasswordUserRequest;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ResultUsersResponse;
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

    @Operation(summary = "Edit user profile", description = "Endpoint to edit the user's profile information")
    @PostMapping("/editUserProfile")
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

    @PreAuthorize("hasAuthority('ADMIN')")    @DeleteMapping()
    @Operation(summary = "Удаление пациента",description = "Метод Удаление пациента по ID")
    public SimpleResponse deleteByUserId(@RequestParam Long id){
        return userService.deletePatientsById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllPatients")
    @Operation(summary = "Страница получение пациентов", description = "Метод получение всех пациентов")
    public List<ResultUsersResponse> responses(){
        return userService.getAllPatients();
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("search")
    @Operation(summary = "Страница получение пациентов по поиску",description = "метод поиска по имени и фио или по email")
    public List<ResultUsersResponse> responsesBySearch(@RequestParam String word){
        return userService.getAllPatientsBySearch(word);
    }
}