package healthcheck.api;
import com.google.firebase.auth.FirebaseAuthException;
import healthcheck.dto.Authentication.AuthenticationResponse;
import healthcheck.dto.Authentication.SignInRequest;
import healthcheck.dto.Authentication.SignUpRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.email.EmailService;
import healthcheck.service.AuthenticationService;
import healthcheck.validation.EmailValidation;
import healthcheck.validation.ValidPassword;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
@Tag(name = "Auth api", description = "API's for Authentication ")
public class AuthApi {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/signUp")
    @Operation(summary = "Sign Up", description = "Register a new user")
    public AuthenticationResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.signUp(signUpRequest);
    }

    @PostMapping("/signIn")
    @Operation(summary = "Sign In", description = "Authenticate and sign in the user")
    public AuthenticationResponse signIn(@RequestBody SignInRequest signInRequest) {
        return authenticationService.signIn(signInRequest);
    }

    @PostMapping("/authenticate/google")
    @Operation(summary = "Authentication with Google", description = "Authentication via Google using Firebase")
    public AuthenticationResponse authWithGoogleAccount(@RequestParam String tokenId) throws FirebaseAuthException {
        return authenticationService.authWithGoogleAccount(tokenId);
    }

    @SneakyThrows
    @Operation(summary = "Send Email", description = "Sends an email to the user with a confirmation link. The user receives the message and clicks the confirmation button.")
    @PostMapping("/send-email")
    public SimpleResponse sendVerificationEmail(@EmailValidation @RequestParam String email, @RequestParam String link) {
        return emailService.forgotPassword(email, link);
    }

    @Operation(summary = "Navigate to Forgot Password Page", description = "When the user clicks 'Confirm,' it redirects to the forgot password page. You need to provide the token and new password data.")
    @PostMapping("/forgot-password")
    public AuthenticationResponse completePasswordRecovery(@RequestParam String token, @ValidPassword @RequestParam String newPassword) {
        return emailService.passwordRecovery(token, newPassword);
    }
}