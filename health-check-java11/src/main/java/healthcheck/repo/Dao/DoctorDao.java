package healthcheck.repo.Dao;
import healthcheck.dto.Doctor.ResponseToGetDoctorsByDepartment;
import java.util.List;

public interface DoctorDao {
    List<ResponseToGetDoctorsByDepartment> getDoctorsByDepartment();
}
