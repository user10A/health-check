package healthcheck.repo;

import healthcheck.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule,Long> {
    @Query("SELECT MAX(endDateWork)FROM Schedule")
    LocalDate getByEndDateWorkSchedule();

}