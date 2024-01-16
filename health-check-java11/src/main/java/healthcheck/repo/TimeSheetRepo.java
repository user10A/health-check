package healthcheck.repo;
import healthcheck.entities.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSheetRepo extends JpaRepository<TimeSheet,Long> {

}