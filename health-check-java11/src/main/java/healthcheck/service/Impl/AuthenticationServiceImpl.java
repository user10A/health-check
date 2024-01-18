package healthcheck.service.Impl;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
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
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        if (userAccountRepo.existsUserAccountByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User with this email already exists");
        }

        UserAccount userAccount = UserAccount.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getNumber())
                .userAccount(userAccount)
                .build();

        userRepo.save(user);

        String jwt = jwtService.generateToken(userAccount.getEmail());

        return AuthenticationResponse.builder()
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .token(jwt)
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {
        UserAccount user = userAccountRepo.getUserAccountByEmail(request.getEmail()).orElseThrow(() ->
                new NotFoundException("Email not found"));

        String passwordBCryp = request.getPassword();
        passwordEncoder.encode(passwordBCryp);

        if (!passwordEncoder.matches(passwordBCryp, user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        String jwt = jwtService.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwt)
                .build();
    }

    @PostConstruct
    void init() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("google.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp.initializeApp(firebaseOptions, "My First Project");
    }

    @Override
    public AuthenticationResponse authWithGoogleAccount(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
        User user;
        if (!userAccountRepo.existsUserAccountByEmail(firebaseToken.getEmail())) {
            User newUser = new User();
            String[] name = firebaseToken.getName().split(" ");
            newUser.setFirstName(name[0]);
            newUser.setLastName(name[1]);
            newUser.setUserAccount(new UserAccount(firebaseToken.getEmail(), firebaseToken.getEmail(), Role.USER));
            userRepo.save(newUser);
        }

        user = userRepo.findUserByUserAccountEmail(firebaseToken.getEmail());
        return new AuthenticationResponse(user.getUserAccount().getEmail(),
                jwtService.generateToken(user.getUserAccount().getEmail()),
                user.getUserAccount().getRole());
    }
}