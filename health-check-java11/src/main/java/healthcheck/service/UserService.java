package healthcheck.service;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ProfileResponse;

public interface UserService {
    ProfileResponse editUserProfile(ProfileRequest profileRequest);
}