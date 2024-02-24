package healthcheck.repo.Dao;

import healthcheck.dto.Schedule.ScheduleDate;
import healthcheck.dto.TimeSheet.TimeSheetResponse;

import java.util.List;

public interface TimeSheetDao {
    List<TimeSheetResponse> getTimesheetDoctor(String facility);
    List<ScheduleDate> getDoctorWorkingDate(String startDate, String endDate, Long doctorId);

}
