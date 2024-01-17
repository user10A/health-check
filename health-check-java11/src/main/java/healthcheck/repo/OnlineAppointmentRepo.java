package healthcheck.repo;
import healthcheck.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineAppointmentRepo extends JpaRepository<Appointment,Long> {

}