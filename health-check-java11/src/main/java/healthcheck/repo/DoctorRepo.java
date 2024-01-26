package healthcheck.repo;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    List<Doctor> getDoctorsByDepartment(Department department);
}