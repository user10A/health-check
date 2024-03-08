package healthcheck.repo.Dao;

import healthcheck.dto.Schedule.ResponseToGetSchedules;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleDao {
    List<ResponseToGetSchedules> getAllSchedules();

    List<ResponseToGetSchedules> getScheduleByDate(LocalDate startDate, LocalDate endDate);

    List<ResponseToGetSchedules> getScheduleBySearch(String word);
}