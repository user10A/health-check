package healthcheck.service;

import healthcheck.dto.TimeSheet.TimeSheetResponse;

import java.util.List;

public interface TimeSheetService {
    List<TimeSheetResponse> getTimesheetDoctor(String facility);


}