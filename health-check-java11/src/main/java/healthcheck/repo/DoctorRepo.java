package healthcheck.repo;
import healthcheck.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctor,Long> {

}