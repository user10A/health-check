package healthcheck.repo;
import healthcheck.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule,Long> {

}