package healthcheck.service.Impl;

import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.*;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.exceptions.InvalidPasswordException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.UserDao;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserDao userDao;
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public SimpleResponse editUserProfile(ProfileRequest profileRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            log.info("Редактирование профиля пользователя с email: {}", email);

            UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);

            if (userAccount != null) {
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
                String successMessage = messageSource.getMessage("update.success", null, Locale.getDefault());
                return SimpleResponse.builder().messageCode(successMessage).httpStatus(HttpStatus.OK).build();
            } else {
                throw new DataUpdateException(messageSource.getMessage("user.not.found", null, Locale.getDefault()));
            }
        } catch (DataUpdateException e) {
            log.error("Ошибка при редактировании профиля пользователя", e);

            throw new RuntimeException(messageSource.getMessage("profile.edit.error", null, Locale.getDefault()), e);
        }

    }

    @Override
    public GetUserResponseByToken responseUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Редактирование профиля пользователя с email: {}", email);
        UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);
        return GetUserResponseByToken.builder()
                .firstName(userAccount.getUser().getFirstName())
                .lastName(userAccount.getUser().getLastName())
                .number(userAccount.getUser().getPhoneNumber())
                .email(userAccount.getEmail())
                .build();
    }

    @Override
    public List<ResponseToGetUserAppointments> getAllAppointmentsOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount user = userAccountRepo.getUserAccountByEmail(email).orElseThrow(() -> {
            log.error(String.format("User with email :%s is not found !!!", email));
            return new NotFoundException(messageSource.getMessage("user.not.found", null, Locale.getDefault()));
        });
        return userDao.getAllAppointmentsOfUser(user.getId());
    }

    @Override
    public ResponseToGetAppointmentByUserId getUserAppointmentById(Long id) {
        return userDao.getUserAppointmentById(id);
    }

    @Override
    @Transactional
    public int clearMyAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount user = userAccountRepo.getUserAccountByEmail(email).orElseThrow(() -> {
            log.error(String.format("User with email %s is not found !!!", email));
            return new NotFoundException(messageSource.getMessage("user.not.found", null, Locale.getDefault()));
        });
        return userDao.clearMyAppointments(user.getId());
    }

    @Override
    public ResponseToGetUserById getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public SimpleResponse changePassword(ChangePasswordUserRequest changePasswordUserRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);
            String oldPassword = changePasswordUserRequest.getOldPassword();

            if (!passwordEncoder.matches(oldPassword, userAccount.getPassword())) {
                throw new InvalidPasswordException(messageSource.getMessage("password.old.invalid", null, Locale.getDefault()));
            }

            if (!changePasswordUserRequest.getNewPassword().equals(changePasswordUserRequest.getResetNewPassword())) {
                throw new InvalidPasswordException(messageSource.getMessage("password.new.mismatch", null, Locale.getDefault()));
            }

            String newPassword = passwordEncoder.encode(changePasswordUserRequest.getNewPassword());
            userAccount.setPassword(newPassword);

            userAccountRepo.save(userAccount);

            log.info("Пароль пользователя успешно изменен");

            String successMessage = messageSource.getMessage("password.change.success", null, Locale.getDefault());
            return SimpleResponse.builder().messageCode(successMessage).httpStatus(HttpStatus.OK).build();
        } catch (DataUpdateException e) {
            log.error("Ошибка при изменении пароля пользователя", e);
            throw new DataUpdateException(messageSource.getMessage("password.change.error", null, Locale.getDefault()));
        }
    }

    @Override
    @Transactional
    public SimpleResponse deletePatientsById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepo.delete(user);
            String deleted = messageSource.getMessage("user.deleted.success", null, Locale.getDefault());
            return new SimpleResponse(deleted, HttpStatus.OK);
        } else {
            String notFound = messageSource.getMessage("error.user_not_found", null, Locale.getDefault());
            return new SimpleResponse(notFound, HttpStatus.OK);
        }
    }

    @Override
    public List<ResultUsersResponse> getAllPatients() {
        return userDao.getAllPatients();
    }

    @Override
    public List<ResultUsersResponse> getAllPatientsBySearch(String word) {
        return userDao.resultUsersBySearch(word);
    }
}