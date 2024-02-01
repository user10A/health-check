package healthcheck.repo.Dao;

import healthcheck.dto.TimeSheet.TimeSheetResponse;

import java.util.List;

public interface TimeSheetDao {
    List<TimeSheetResponse> getTimesheetDoctor(String facility);
}
