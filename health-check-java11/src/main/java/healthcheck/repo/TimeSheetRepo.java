package healthcheck.repo;
import healthcheck.entities.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSheetRepo extends JpaRepository<TimeSheet,Long> {

}