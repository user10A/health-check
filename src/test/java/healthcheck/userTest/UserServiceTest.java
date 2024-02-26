package healthcheck.userTest;

import healthcheck.HealthCheckJava11Application;
import healthcheck.dto.Authentication.SignInRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.*;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.repo.Dao.UserDao;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.UserService;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.aspectj.bridge.MessageUtil.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = HealthCheckJava11Application.class)
@Slf4j
public class UserServiceTest {
    private static final String BASE_URI = "http://localhost:2024/api/user";
    private static final String AUTH_BASE_URI = "http://localhost:2024/api/auth";
    private static final String ADMIN_PASSWORD = "Abcd123!@";
    private final UserService userService;
    private final UserAccountRepo userAccount;
    private final UserRepo userRepo;
    private final UserDao userDao;

    @Autowired
    public UserServiceTest(UserService userService, UserAccountRepo userAccount, UserRepo userRepo, UserDao userDao) {
        this.userService = userService;
        this.userAccount = userAccount;
        this.userRepo = userRepo;
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

            assertEquals("Успешно изменено!", response.getMessage());
            assertEquals(HttpStatus.OK, response.getHttpStatus());

        } catch (Exception e) {
            log.error("Ошибка при выполнении теста: " + e.getMessage());
            throw e;
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    @Test
    void testEditUserProfile_EmailNotEqual() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("newemail@example.com");

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail("oldemail@example.com");

        userService.editUserProfile(profileRequest);

        assertEquals(profileRequest.getEmail(), userAccount.getEmail());
        verify(userAccount).setUser(userAccount.getUser());
    }

    @Test
    void testEditUserProfile_NumberPhoneNotEqual() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setNumberPhone("1234567890");

        UserAccount userAccount = new UserAccount();
        User user = new User();
        user.setPhoneNumber("0987654321");
        userAccount.setUser(user);

        userService.editUserProfile(profileRequest);

        assertEquals(profileRequest.getNumberPhone(), user.getPhoneNumber());
        verify(userRepo).save(user);
    }

    @Test
    void testEditUserProfile_FirstNameNotEqual() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setFirstName("John");

        UserAccount userAccount = new UserAccount();
        User user = new User();
        user.setFirstName("Jane");
        userAccount.setUser(user);

        userService.editUserProfile(profileRequest);

        assertEquals(profileRequest.getFirstName(), user.getFirstName());
        verify(userRepo).save(user);
    }

    @Test
    void testEditUserProfile_LastNameNotEqual() {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setLastName("Doe");

        UserAccount userAccount = new UserAccount();
        User user = new User();
        user.setLastName("Smith");
        userAccount.setUser(user);

        userService.editUserProfile(profileRequest);

        assertEquals(profileRequest.getLastName(), user.getLastName());
        verify(userRepo).save(user);
    }

    @Test
    void testEditUserProfile_DataUpdateException() {
        ProfileRequest profilerequest = new ProfileRequest();
        assertThrows(DataUpdateException.class, () -> userService.editUserProfile(profilerequest));
    }



    // МЕТОД public List<ResponseToGetUserAppointments> getAllAppointmentsOfUser();

    @Test
    @DisplayName("Получение всех назначений user")
    public void getAllAppointmentsOfUser() {
        try {
            String authToken = given()
                    .contentType(ContentType.JSON)
                    .body(new SignInRequest("ella.martin@gmail.com", ADMIN_PASSWORD))
                    .when()
                    .post(AUTH_BASE_URI + "/signIn")
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("token");

            List<ResponseToGetUserAppointments> appointments = given()
                    .auth().oauth2(authToken)
                    .when()
                    .get(BASE_URI + "/getAllAppointmentsOfUser")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", ResponseToGetUserAppointments.class);

            log.info("Полученные приемы: {}", appointments);

            assertThat(appointments, hasSize(greaterThan(0)));

            for (ResponseToGetUserAppointments appointment : appointments) {
                log.info("Проверка приема: {}", appointment);
                assertThat(appointment, allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("appointmentDate", notNullValue()),
                        hasProperty("appointmentTime", notNullValue()),
                        hasProperty("status", notNullValue()),
                        hasProperty("surname", notNullValue()),
                        hasProperty("department", notNullValue()),
                        hasProperty("image", notNullValue())
                ));
            }
        } catch (Exception e) {
            log.error("Произошла ошибка в тесте", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Получение записей по id user")
    public void getUserAppointmentById() {
        Long id = 11L;

        try {
            ResponseToGetAppointmentByUserId response = userDao.getUserAppointmentById(id);

            log.info("Проверка приема: {}", response);

            assertThat(response, allOf(
                    hasProperty("appointmentDate", notNullValue()),
                    hasProperty("appointmentTime", notNullValue()),
                    hasProperty("surnameOfDoctor", notNullValue()),
                    hasProperty("department", notNullValue()),
                    hasProperty("first_name", notNullValue()),
                    hasProperty("last_name", notNullValue()),
                    hasProperty("phone_number", notNullValue()),
                    hasProperty("email", notNullValue()),
                    hasProperty("image", notNullValue())
            ));
        } catch (Exception e) {
            log.error("Произошла ошибка в тесте", e);
            throw e;
        }
    }

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

            assertEquals("Успешно изменен пароль!", simpleResponse.getMessage());
            assertEquals(HttpStatus.OK, simpleResponse.getHttpStatus());
        } catch (Exception e) {
            log.error("Произошла ошибка в тесте", e);
            throw e;
        }
    }

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
    @DisplayName("Удаление существующего пользователя")
    public void deletePatientById() {
        Long id = 5L;

        try {
            log.info("Выполняется тест удаления пациента по идентификатору: {}", id);

            SimpleResponse simpleResponse = userService.deletePatientsById(id);

            assertEquals("Пользователь успешно удален", simpleResponse.getMessage());
            assertEquals(HttpStatus.OK, simpleResponse.getHttpStatus());

            log.info("Тест удаления пациента по идентификатору пройден успешно");
        } catch (Exception e) {
            log.error("Тест удаления пациента по идентификатору завершился с ошибкой", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Получение всех докторов")
    public void getAllPatients() {
        String authToken = given().contentType(ContentType.JSON)
                .body(new SignInRequest("admin@gmail.com", ADMIN_PASSWORD))
                .when()
                .post(AUTH_BASE_URI + "/signIn")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        try {
            List<ResultUsersResponse> responses = given().auth().oauth2(authToken)
                    .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .when()
                    .get(BASE_URI + "/getAllPatients")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", ResultUsersResponse.class);

            log.info("Получены записи о приемах: {}", responses);

            assertThat(responses, hasSize(greaterThan(0)));

            for (ResultUsersResponse response : responses) {
                log.info("Проверка вывода информации о врачах: {}", response);
                assertThat(response, allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("surname", notNullValue()),
                        hasProperty("phoneNumber", notNullValue()),
                        hasProperty("email", notNullValue()),
                        hasProperty("resultDate", notNullValue())
                ));
            }
        } catch (Exception e) {
            log.error("Тест на получение всех пациентов завершился с ошибкой", e);
            fail("Тест на получение всех пациентов завершился с ошибкой: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Получение всех пациентов по поиску")
    public void getAllPatientsBySearch() {
        String authToken = given().contentType(ContentType.JSON)
                .body(new SignInRequest("admin@gmail.com", ADMIN_PASSWORD))
                .when()
                .post(AUTH_BASE_URI + "/signIn")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        try {
            String word = "A";

            List<ResultUsersResponse> usersResponses = given().auth().oauth2(authToken)
                    .queryParam("word", word)
                    .when()
                    .get(BASE_URI + "/search")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", ResultUsersResponse.class);

            log.info("Результаты поиска для слова '{}': {}", word, usersResponses);

            assertThat(usersResponses, hasSize(greaterThan(0)));

            for (ResultUsersResponse response : usersResponses) {
                log.info("Проверка вывода информации о пациенте: {}", response);
                assertThat(response, allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("surname", notNullValue()),
                        hasProperty("phoneNumber", notNullValue()),
                        hasProperty("email", notNullValue()),
                        hasProperty("resultDate", notNullValue())
                ));
            }
        } catch (Exception e) {
            log.error("Тест на поиск пациентов завершился с ошибкой", e);
            fail("Тест на поиск пациентов завершился с ошибкой: " + e.getMessage());
        }
    }
}