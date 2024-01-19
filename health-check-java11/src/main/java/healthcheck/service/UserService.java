package healthcheck.service;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.ChangePasswordUserRequest;
import healthcheck.dto.User.ProfileRequest;

public interface UserService {
    SimpleResponse editUserProfile(ProfileRequest profileRequest);
    SimpleResponse changePassword(ChangePasswordUserRequest changePasswordUserRequest);
}