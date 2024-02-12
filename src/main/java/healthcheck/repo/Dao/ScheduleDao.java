package healthcheck.repo.Dao;

import healthcheck.dto.Schedule.ResponseToGetSchedules;

import java.util.List;

public interface ScheduleDao {
    List<ResponseToGetSchedules> getAllSchedules();
    List<ResponseToGetSchedules> getScheduleByDate(String startDate, String endDate);
    List<ResponseToGetSchedules> getScheduleBySearch(String word);
}