package healthcheck.repo.Dao;
import healthcheck.dto.User.ResultUsersResponse;
import healthcheck.dto.User.UserResponse;
import healthcheck.dto.User.UserResponseGetById;
import java.util.List;

public interface UserDao {
    List<ResultUsersResponse> getAllPatients();
    List<UserResponse> getAllAppointmentsOfUser(Long id);
    UserResponseGetById getById(Long id);
    int clearMyAppointments(Long id);

}
