package healthcheck.userTest;

import healthcheck.HealthCheckJava11Application;
import healthcheck.dto.Authentication.SignInRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.*;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.repo.Dao.UserDao;
import healthcheck.repo.UserAccountRepo;
import healthcheck.service.UserService;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = HealthCheckJava11Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class UserServiceTest {
    private static final String BASE_URI = "http://localhost:2024/api/user";
    private static final String AUTH_BASE_URI = "http://localhost:2024/api/auth";
    private static final String ADMIN_PASSWORD = "Abcd123!@";
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

        assertEquals("Успешно изменено!", response.getMessage());
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

        assertEquals("Успешно изменено!", response.getMessage());
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

        assertEquals("Успешно изменено!", response.getMessage());
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

        assertEquals("Успешно изменено!", response.getMessage());
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
    @DisplayName("Получение онлайн записей по user: исключение")
    public void testGetAllAppointment_NotFoundException() {
        ProfileRequest profileRequest = new ProfileRequest();

        Authentication authentication = new UsernamePasswordAuthenticationToken("jasona.miller@gmail.com", "Abcd123!@");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        DataUpdateException exception = assertThrows(DataUpdateException.class, () ->
                userService.editUserProfile(profileRequest));

        assertEquals("Ошибка при редактировании профиля пользователя", exception.getMessage());
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

            assertEquals("User successfully deleted", simpleResponse.getMessage());
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