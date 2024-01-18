package healthcheck.api;

import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ProfileResponse;
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
@RequestMapping("/user")
public class UserApi {

    private final UserService userService;

    @Operation(summary = "Edit user profile", description = "Endpoint to edit the user's profile information")
    @PostMapping("/editUserProfile")
    @PostAuthorize("hasAuthority('USER')")
    public ProfileResponse editUserProfile(@RequestBody ProfileRequest profileRequest){
        return userService.editUserProfile(profileRequest);
    }
}