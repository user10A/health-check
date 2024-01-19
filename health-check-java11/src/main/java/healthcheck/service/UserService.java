package healthcheck.service;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ProfileResponse;
import healthcheck.dto.User.UserResponse;
import healthcheck.dto.User.UserResponseGetById;
import java.util.List;

public interface UserService {
    ProfileResponse editUserProfile(ProfileRequest profileRequest);
    List<UserResponse> getAllAppointmentsOfUser();
    UserResponseGetById getById(Long id);
    int clearMyAppointments();
}