package healthcheck.service;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.ChangePasswordUserRequest;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ProfileResponse;
import healthcheck.dto.User.UserResponse;
import healthcheck.dto.User.UserResponseGetById;
import java.util.List;

public interface UserService {
    SimpleResponse editUserProfile(ProfileRequest profileRequest);
    SimpleResponse changePassword(ChangePasswordUserRequest changePasswordUserRequest);
    List<UserResponse> getAllAppointmentsOfUser();
    UserResponseGetById getById(Long id);
    int clearMyAppointments();

}