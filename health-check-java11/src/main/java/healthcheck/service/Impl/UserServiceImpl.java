package healthcheck.service.Impl;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.ChangePasswordUserRequest;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.exceptions.InvalidPasswordException;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SimpleResponse editUserProfile(ProfileRequest profileRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            log.info("Редактирование профиля пользователя с email: {}", email);

            UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);

            if (!userAccount.getEmail().equals(profileRequest.getEmail())) {
                userAccount.setEmail(profileRequest.getEmail());
            }
            if (!userAccount.getUser().getPhoneNumber().equals(profileRequest.getNumberPhone())) {
                userAccount.getUser().setPhoneNumber(profileRequest.getNumberPhone());
            }

            if (!userAccount.getUser().getFirstName().equals(profileRequest.getFirstName())) {
                userAccount.getUser().setFirstName(profileRequest.getFirstName());
            }
            if (!userAccount.getUser().getLastName().equals(profileRequest.getLastName())) {
                userAccount.getUser().setLastName(profileRequest.getLastName());
            }

            userRepo.save(userAccount.getUser());
            userAccountRepo.save(userAccount);

            log.info("Профиль пользователя успешно изменен");

            return SimpleResponse.builder().message("Успешно изменено!").httpStatus(HttpStatus.OK).build();
        } catch (DataUpdateException e) {
            log.error("Ошибка при редактировании профиля пользователя", e);
            throw new RuntimeException("Ошибка при редактировании профиля пользователя", e);
        }
    }

    @Override
    public SimpleResponse changePassword(ChangePasswordUserRequest changePasswordUserRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);
            String oldPassword = changePasswordUserRequest.getOldPassword();

            if (!passwordEncoder.matches(oldPassword, userAccount.getPassword())) {
                if (!changePasswordUserRequest.getNewPassword().equals(changePasswordUserRequest.getResetNewPassword())) {
                    throw new InvalidPasswordException("Ошибка в новом пароле");
                }
                throw new InvalidPasswordException("Ошибка в старом пароле");
            }

            String newPassword = passwordEncoder.encode(changePasswordUserRequest.getNewPassword());
            userAccount.setPassword(newPassword);

            userAccountRepo.save(userAccount);

            log.info("Пароль пользователя успешно изменен");

            return SimpleResponse.builder().message("Успешно изменен пароль!").httpStatus(HttpStatus.OK).build();
        } catch (DataUpdateException e) {
            log.error("Ошибка при изменении пароля пользователя", e);
            throw new RuntimeException("Ошибка при изменении пароля пользователя", e);
        }
    }
}