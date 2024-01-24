package healthcheck.repo.Dao;
import healthcheck.dto.User.ResponseToGetUserById;
import healthcheck.dto.User.ResultUsersResponse;
import healthcheck.dto.User.ResponseToGetUserAppointments;
import healthcheck.dto.User.ResponseToGetAppointmentByUserId;
import java.util.List;

public interface UserDao {
    List<ResultUsersResponse> getAllPatients();
    List<ResponseToGetUserAppointments> getAllAppointmentsOfUser(Long id);
    ResponseToGetAppointmentByUserId getUserAppointmentById(Long id);
    int clearMyAppointments(Long id);
    ResponseToGetUserById getUserById(Long id);
}
