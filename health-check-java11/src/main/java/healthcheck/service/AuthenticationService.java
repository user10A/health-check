package healthcheck.service;
import healthcheck.dto.Authentication.AuthenticationResponse;
import healthcheck.dto.Authentication.SignInRequest;
import healthcheck.dto.Authentication.SignUpRequest;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);

    AuthenticationResponse signIn(SignInRequest request);
}