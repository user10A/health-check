package healthcheck.api;

import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.ChangePasswordUserRequest;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}