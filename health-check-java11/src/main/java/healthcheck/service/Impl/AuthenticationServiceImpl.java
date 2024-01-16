package healthcheck.service.Impl;
import healthcheck.config.JwtService;
import healthcheck.dto.Authentication.AuthenticationResponse;
import healthcheck.dto.Authentication.SignInRequest;
import healthcheck.dto.Authentication.SignUpRequest;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.enums.Role;
import healthcheck.exceptions.AlreadyExistsException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse signUp(SignUpRequest request) {
        UserAccount userAccount1 = (UserAccount) userAccountRepo.findUserAccountByEmail(request.getEmail());

        if(userAccount1 != null){
            throw new AlreadyExistsException("This user exists");
        }

        UserAccount userAccount = new UserAccount();

        userAccount.setEmail(request.getEmail());
        userAccount.setPassword(request.getPassword());
        userAccount.setRole(Role.USER);

        User user = new User();
        user.setFirstName(request.getFirstNameUp());
        user.setLastName(request.getLastNameUp());
        user.setPhoneNumber(request.getNumberUp());
        user.setUserAccount(userAccount);
        userRepo.save(user);

        userAccount.setUser(user);
        userAccountRepo.save(userAccount);

        String jwt = jwtService.generateToken(userAccount);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setEmail(userAccount.getEmail());
        authenticationResponse.setRole(userAccount.getRole());
        authenticationResponse.setToken(jwt);
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {
        UserAccount user = userAccountRepo.getUserByEmail(request.getEmail()).orElseThrow(() ->
                new NotFoundException("Email not found"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        String jwt = jwtService.generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setEmail(user.getEmail());
        authenticationResponse.setRole(user.getRole());
        authenticationResponse.setToken(jwt);

        return authenticationResponse;
    }
}