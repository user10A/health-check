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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
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
    @Operation(summary = "Отправление сообщения в емаил", description = "сообщения приходит в User и нажимает кнопку подтвердить")
    @PostMapping("/send-email")
    public SimpleResponse sendVerificationEmail(@EmailValidation @RequestParam String email, @RequestParam String link) {
        return emailService.forgotPassword(email,link);
    }
    @Operation(summary = "При нажатии подтвердить переходит в page forgot password", description = "и зачем вы передаете нам token и даныые пароля")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> completePasswordRecovery(@RequestParam String token, @ValidPassword @RequestParam String newPassword) {
        return emailService.passwordRecovery(token,newPassword);
    }
}
