package healthcheck.service;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.User.*;

import java.util.List;

public interface UserService {
    SimpleResponse editUserProfile(ProfileRequest profileRequest);
    SimpleResponse changePassword(ChangePasswordUserRequest changePasswordUserRequest);
    SimpleResponse deletePatientsById(Long id);
    List<ResultUsersResponse> getAllPatients() ;
    List<ResultUsersResponse> getAllPatientsBySearch(String word);
    List<ResponseToGetUserAppointments> getAllAppointmentsOfUser();
    ResponseToGetAppointmentByUserId getUserAppointmentById(Long id);
    int clearMyAppointments();
    ResponseToGetUserById getUserById(Long id);
}