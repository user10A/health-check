package healthcheck.service.Impl;

import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.*;
import healthcheck.entities.Appointment;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.exceptions.InvalidPasswordException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.AppointmentRepo;
import healthcheck.repo.Dao.UserDao;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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
    private final AppointmentRepo appointmentRepo;

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
                return SimpleResponse.builder().messageCode(messageSource.getMessage("update.success",
                        null, LocaleContextHolder.getLocale())).httpStatus(HttpStatus.OK).build();
            } else {
                throw new DataUpdateException("user.not.found");
            }
        } catch (DataUpdateException e) {
            log.error("Ошибка при редактировании профиля пользователя", e);
            throw new RuntimeException("profile.edit.error");
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
            return new NotFoundException("user.not.found");
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
            return new NotFoundException("user.not.found");
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
                throw new InvalidPasswordException("password.old.invalid");
            }

            if (!changePasswordUserRequest.getNewPassword().equals(changePasswordUserRequest.getResetNewPassword())) {
                throw new InvalidPasswordException("password.new.mismatch");
            }
            String newPassword = passwordEncoder.encode(changePasswordUserRequest.getNewPassword());
            userAccount.setPassword(newPassword);

            userAccountRepo.save(userAccount);

            log.info("Пароль пользователя успешно изменен");
            return SimpleResponse.builder().messageCode(messageSource.getMessage("password.change.success",
                    null,LocaleContextHolder.getLocale())).httpStatus(HttpStatus.OK).build();
        } catch (DataUpdateException e) {
            log.error("Ошибка при изменении пароля пользователя", e);
            throw new DataUpdateException("password.change.error");
        }
    }
    @Override
    @Transactional
    public SimpleResponse deletePatientsById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepo.delete(user);
            return new SimpleResponse(messageSource.getMessage("user.deleted.success",
                    null,LocaleContextHolder.getLocale()), HttpStatus.OK);
        } else {
            return new SimpleResponse(messageSource.getMessage("error.user_not_found",
                    null,LocaleContextHolder.getLocale()), HttpStatus.OK);
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