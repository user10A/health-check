package healthcheck.userTest;

import healthcheck.HealthCheckJava11Application;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.*;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.exceptions.InvalidPasswordException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.UserDao;
import healthcheck.repo.UserAccountRepo;
import healthcheck.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = HealthCheckJava11Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class UserServiceTest {
    private final UserService userService;
    private final UserAccountRepo userAccount;
    private final UserDao userDao;

    @Autowired
    public UserServiceTest(UserService userService, UserAccountRepo userAccount, UserDao userDao) {
        this.userService = userService;
        this.userAccount = userAccount;
        this.userDao = userDao;
    }

    // МЕТОД public SimpleResponse editUserProfile(ProfileRequest profileRequest);
    @Test
    @DisplayName("Редактирование профиля user")
    public void editUserProfile() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("kas@gmail.com");
        profileRequest.setFirstName("Ad");
        profileRequest.setLastName("lA");
        profileRequest.setNumberPhone("+996997664490");

        Authentication authentication = new UsernamePasswordAuthenticationToken("jason.miller@gmail.com", "Abcd123!@");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            SimpleResponse response = userService.editUserProfile(profileRequest);

//            assertEquals("Успешно изменено!", response.getMessage());
            assertEquals(HttpStatus.OK, response.getHttpStatus());

        } catch (Exception e) {
            log.error("Ошибка при выполнении теста: " + e.getMessage());
            throw e;
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Редактирование профиля: изменение Email")
    public void testEditUserProfileChangeEmail() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setNumberPhone("+996999999999");
        profileRequest.setEmail("newEmail@gmail.com");
        profileRequest.setFirstName("newFirstName");
        profileRequest.setLastName("newLastName");

        Authentication authentication = new UsernamePasswordAuthenticationToken("sarah.miller@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SimpleResponse response = userService.editUserProfile(profileRequest);

//        assertEquals("Успешно изменено!", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        UserAccount updatedUserAccount = userAccount.findUserAccountByEmail("newEmail@gmail.com");
        assertNotNull(updatedUserAccount);
        assertEquals("newEmail@gmail.com", updatedUserAccount.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Редактирование профиля: изменение PhoneNumber")
    public void testEditUserProfileChangePhoneNumber() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("newEmail@gmail.com");
        profileRequest.setNumberPhone("+996123456789");
        profileRequest.setFirstName("newFirstName");
        profileRequest.setLastName("newLastName");

        Authentication authentication = new UsernamePasswordAuthenticationToken("newEmail@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SimpleResponse response = userService.editUserProfile(profileRequest);

//        assertEquals("Успешно изменено!", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        UserAccount updatedUserAccount = userAccount.findUserAccountByEmail("newEmail@gmail.com");
        assertNotNull(updatedUserAccount);

        assertNotNull(updatedUserAccount.getUser());

        assertEquals("+996123456789", updatedUserAccount.getUser().getPhoneNumber());
    }

    @Test
    @Order(3)
    @DisplayName("Редактирование профиля: изменение Имени")
    public void testEditUserProfileChangeFirstName() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setNumberPhone("+996123456789");
        profileRequest.setFirstName("newFirstName");
        profileRequest.setLastName("newLastName");
        profileRequest.setEmail("newEmail@gmail.com");

        Authentication authentication = new UsernamePasswordAuthenticationToken("newEmail@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SimpleResponse response = userService.editUserProfile(profileRequest);

//        assertEquals("Успешно изменено!", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        UserAccount userAccount1 = userAccount.findUserAccountByEmail(authentication.getName());

        assertEquals("newFirstName", userAccount1.getUser().getFirstName());
    }

    @Test
    @Order(4)
    @DisplayName("Редактирование профиля: изменение Фамилии")
    public void testEditUserProfileChangeLastName() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("newEmail@gmail.com");
        profileRequest.setNumberPhone("+996123456789");
        profileRequest.setFirstName("newFirstName");
        profileRequest.setLastName("newLastName");

        Authentication authentication = new UsernamePasswordAuthenticationToken("newEmail@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SimpleResponse response = userService.editUserProfile(profileRequest);

//        assertEquals("Успешно изменено!", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        UserAccount userAccount1 = userAccount.findUserAccountByEmail(authentication.getName());

        assertEquals("newLastName", userAccount1.getUser().getLastName());

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Редактирование профиля: исключение")
    public void testEditUserProfile_DataUpdateException() {
        ProfileRequest profileRequest = new ProfileRequest();

        Authentication authentication = new UsernamePasswordAuthenticationToken("jasona.miller@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        DataUpdateException exception = assertThrows(DataUpdateException.class, () ->
            userService.editUserProfile(profileRequest));

        assertEquals("Ошибка при редактировании профиля пользователя", exception.getMessage());
    }



    // МЕТОД public List<ResponseToGetUserAppointments> getAllAppointmentsOfUser();
    @Test
    @DisplayName("Получение всех назначений user")
    public void getAllAppointmentsOfUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("charlie.brown@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<ResponseToGetUserAppointments> response = userService.getAllAppointmentsOfUser();

        assertEquals(userService.getAllAppointmentsOfUser(), response);
    }

    @Test
    @DisplayName("Получение всех назначений user: исключение")
    public void testGetAllAppointment_NotFoundException() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("chalie.brown@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        NotFoundException exception = assertThrows(NotFoundException.class, userService::getAllAppointmentsOfUser);

        assertEquals("User is not found !!!", exception.getMessage());
    }


    // МЕТОД public ResponseToGetAppointmentByUserId getUserAppointmentById();
    @Test
    @DisplayName("Получение записей по id user")
    public void getUserAppointmentById() {
       Long id = 11L;

       Authentication authentication = new UsernamePasswordAuthenticationToken("admin@gmail.com", "Abcd123!@");
       SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseToGetAppointmentByUserId response = userService.getUserAppointmentById(id);

        assertEquals(userDao.getUserAppointmentById(id), response);
    }


    // МЕТОД public SimpleResponse changePassword();
    @Test
    @DisplayName("Смена пароля")
    public void changePassword() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("grace.thompson@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ChangePasswordUserRequest request = new ChangePasswordUserRequest();
        request.setOldPassword("Abcd123!@");
        request.setNewPassword("Xemyru@2007");
        request.setResetNewPassword("Xemyru@2007");

        try {
            SimpleResponse simpleResponse = userService.changePassword(request);

//            assertEquals("Успешно изменен пароль!", simpleResponse.getMessage());
            assertEquals(HttpStatus.OK, simpleResponse.getHttpStatus());
        } catch (Exception e) {
            log.error("Произошла ошибка в тесте", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Смена пароля: исключение (старый пароль)")
    public void testChangeOldPassword_InvalidPasswordException() {
        ChangePasswordUserRequest request = new ChangePasswordUserRequest();
        request.setOldPassword("Abcd123!");
        request.setNewPassword("Abcd123!@@");
        request.setResetNewPassword("Abcd123!@@");

        Authentication authentication = new UsernamePasswordAuthenticationToken("david.thomas@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            log.info("Executing testChangeOldPassword_InvalidPasswordException");
            InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () ->
                    userService.changePassword(request));
            assertEquals("Ошибка в старом пароле", exception.getMessage());
        } catch (Exception e) {
            log.error("An unexpected exception occurred in testChangeOldPassword_InvalidPasswordException", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Смена пароля: исключение (новый пароль)")
    public void testChangeNewPassword_InvalidPasswordException() {
        ChangePasswordUserRequest request = new ChangePasswordUserRequest();
        request.setOldPassword("Abcd123!@");
        request.setNewPassword("Abcd123!@1");
        request.setResetNewPassword("DifferentPassword");

        Authentication authentication = new UsernamePasswordAuthenticationToken("david.thomas@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            log.info("Executing testChangeNewPassword_InvalidPasswordException");
            userService.changePassword(request);
            fail("Ожидалось выбрасывание исключения InvalidPasswordException");
        } catch (InvalidPasswordException exception) {
            log.info("Expected exception: InvalidPasswordException. Message: " + exception.getMessage());
            assertEquals("Ошибка в новом пароле", exception.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception: " + e.getMessage(), e);
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Смена пароля: исключение (DataUpdate пароль)")
    public void testChangePassword_DataUpdateException() {
        ChangePasswordUserRequest request = new ChangePasswordUserRequest();
        request.setOldPassword("Abcd123!");
        request.setNewPassword("Abcd123!@@");
        request.setResetNewPassword("Abcd123!@@");

        Authentication authentication = new UsernamePasswordAuthenticationToken("david.thomas@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            log.info("Executing testChangePassword_DataUpdateException");
            DataUpdateException exception = assertThrows(DataUpdateException.class, () ->
                    userService.changePassword(request));
            assertEquals("Ошибка при изменении пароля пользователя", exception.getMessage());
        } catch (Exception e) {
            log.error("An unexpected exception occurred in testChangeOldPassword_InvalidPasswordException", e);
            throw e;
        }
    }


    // МЕТОД public ResponseToGetUserById getUserById(Long id);
    @Test
    @DisplayName("Получение user-а по id")
    public void getUserById() {
        Long id = 14L;

        try {
            ResponseToGetUserById response = userService.getUserById(id);

            assertThat(response, allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("first_name", notNullValue()),
                    hasProperty("last_name", notNullValue()),
                    hasProperty("email", notNullValue()),
                    hasProperty("phone_number", notNullValue())
            ));
            log.info("Получены данные пользователя с идентификатором {}", id);
        } catch (Exception e) {
            log.error("Произошла ошибка в тесте", e);
            throw e;
        }
    }


    // МЕТОД public int clearMyAppointments();
    @Test
    @DisplayName("Очистка записей")
    public void clearMyAppointments() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("nathan.morris@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String email = authentication.getName();
        try {
            UserAccount account = userAccount.findUserAccountByEmail(email);

            int response = userDao.clearMyAppointments(account.getId());

            assertEquals(1, response);
            log.info("Очищены назначения для пользователя с идентификатором {}", account.getId());
        } catch (Exception e) {
            log.error("Произошла ошибка в тесте", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Очистка записей")
    public void clearMyAppointments_UserDao() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("nathan.morris@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String email = authentication.getName();
        try {
            UserAccount account = userAccount.findUserAccountByEmail(email);

            assertEquals(userDao.clearMyAppointments(account.getId()), userService.clearMyAppointments());
            log.info("Очищены назначения для пользователя с идентификатором {}", account.getId());
        } catch (Exception e) {
            log.error("Произошла ошибка в тесте", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Очистка записей: исключение")
    public void clearMyAppointments_NotFoundException() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("natan.morris@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        NotFoundException exception = assertThrows(NotFoundException.class, userService::clearMyAppointments);

        assertEquals("User is not found !!!", exception.getMessage());
    }


    // МЕТОД  public SimpleResponse deletePatientsById(Long id);
    @Test
    @DisplayName("Удаление существующего пользователя")
    public void deletePatientById() {
        Long id = 5L;

        try {
            log.info("Выполняется тест удаления пациента по идентификатору: {}", id);

            SimpleResponse simpleResponse = userService.deletePatientsById(id);

//            assertEquals("User successfully deleted", simpleResponse.getMessage());
            assertEquals(HttpStatus.OK, simpleResponse.getHttpStatus());

            log.info("Тест удаления пациента по идентификатору пройден успешно");
        } catch (Exception e) {
            log.error("Тест удаления пациента по идентификатору завершился с ошибкой", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Удаление существующего пользователя: исключение")
    public void deletePatientById_SimpleResponse() {
        Long id = 99L;

        try {
            log.info("Выполняется тест удаления пациента по идентификатору: {}", id);

            SimpleResponse simpleResponse = userService.deletePatientsById(id);

//            assertEquals("User not found", simpleResponse.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, simpleResponse.getHttpStatus());

            log.info("Тест исключения пациента по идентификатору пройден успешно");
        } catch (Exception e) {
            log.error("Тест удаления пациента по идентификатору завершился с удачно", e);
            throw e;
        }
    }


    // МЕТОД public List<ResultUsersResponse> getAllPatients();
    @Test
    @DisplayName("Получение всех докторов")
    public void getAllPatients() {
      Authentication authentication = new UsernamePasswordAuthenticationToken("admin@gmail.com", "Abcd123!@");
      SecurityContextHolder.getContext().setAuthentication(authentication);

      List<ResultUsersResponse> responses = userService.getAllPatients();

      assertEquals(userDao.getAllPatients(), responses);
    }


    // МЕТОД public List<ResultUsersResponse> getAllPatientsBySearch(String word);
    @Test
    @DisplayName("Получение всех пациентов по поиску")
    public void getAllPatientsBySearch() {
        String word = "A";

        Authentication authentication = new UsernamePasswordAuthenticationToken("admin@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<ResultUsersResponse> responses = userService.getAllPatientsBySearch(word);

        assertEquals(userDao.resultUsersBySearch(word), responses);
    }
}