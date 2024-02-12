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
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse signUp(SignUpRequest request) {
        try {
            if (userAccountRepo.existsUserAccountByEmail(request.getEmail())) {
                throw new AlreadyExistsException("Пользователь с этим адресом электронной почты уже существует");
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

            log.info("Пользователь успешно зарегистрирован: {}", userAccount.getEmail());

            return AuthenticationResponse.builder()
                    .email(userAccount.getEmail())
                    .role(userAccount.getRole())
                    .token(jwt)
                    .build();
        } catch (Exception e) {
            log.error("Ошибка во время регистрации: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {
        try {
            UserAccount user = userAccountRepo.getUserAccountByEmail(request.getEmail()).orElseThrow(() ->
                    new NotFoundException("Электронная почта не найдена"));

            String passwordBCrypt = request.getPassword();
            passwordEncoder.encode(passwordBCrypt);

            if (!passwordEncoder.matches(passwordBCrypt, user.getPassword())) {
                throw new BadCredentialsException("Неверный пароль");
            }

            String jwt = jwtService.generateToken(user.getEmail());

            log.info("Пользователь успешно вошел в систему: {}", user.getEmail());

            return AuthenticationResponse.builder()
                    .email(user.getEmail())
                    .role(user.getRole())
                    .token(jwt)
                    .build();
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostConstruct
    void init() throws IOException {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource("google.json").getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp.initializeApp(firebaseOptions);

            log.info("FirebaseApp успешно инициализирован");
        } catch (IOException e) {
            log.error("Ошибка при инициализации FirebaseApp: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AuthenticationResponse authWithGoogleAccount(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);

        if (userAccountRepo.existsUserAccountByEmail(firebaseToken.getEmail())) {
            String message = "user with email: " + firebaseToken.getEmail() + " is already exists!!";
            log.error(message);
            throw new AlreadyExistsException(message);
        }

        log.info("Creating a new user for email: {}", firebaseToken.getEmail());
        User newUser = new User();
        String[] name = firebaseToken.getName().split(" ");
        newUser.setFirstName(name[0]);
        newUser.setLastName(name[1]);
        userRepo.save(newUser);

        UserAccount userInfo = new UserAccount();
        userInfo.setEmail(firebaseToken.getEmail());
        userInfo.setPassword(firebaseToken.getEmail());
        userInfo.setRole(Role.USER);
        newUser.setUserAccount(userInfo);
        userAccountRepo.save(userInfo);
        log.info("User created successfully for email: {}", firebaseToken.getEmail());

        UserAccount userInformation = newUser.getUserAccount();
        String accessToken = jwtService.generateToken(String.valueOf(userInformation));

        log.info("Authentication successful for email: {}", firebaseToken.getEmail());
        return AuthenticationResponse.builder()
                .token(accessToken)
                .email(firebaseToken.getEmail())
                .role(userInformation.getRole())
                .build();
    }
}