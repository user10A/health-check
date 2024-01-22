package healthcheck.service;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.ChangePasswordUserRequest;
import healthcheck.dto.User.ProfileRequest;
import healthcheck.dto.User.ResultUsersResponse;

import java.util.List;

public interface UserService {
    SimpleResponse editUserProfile(ProfileRequest profileRequest);
    SimpleResponse changePassword(ChangePasswordUserRequest changePasswordUserRequest);
    SimpleResponse deletePatientsById(Long id);
    List<ResultUsersResponse> getAllPatients() ;
    List<ResultUsersResponse> getAllPatientsBySearch(String word);
}