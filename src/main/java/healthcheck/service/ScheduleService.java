package healthcheck.service;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.Schedule.ScheduleUpdateRequest;
import healthcheck.dto.Schedule.TimeSheetDeleteRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.Schedule.PatternTimeSheetRequest;
import healthcheck.enums.Facility;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    SimpleResponse saveSchedule(Facility facility, Long doctorId, AddScheduleRequest addScheduleRequest);
    SimpleResponse updateScheduleByDoctorId(Long doctorId, LocalDate date, List<ScheduleUpdateRequest> timeSlots);
    List<ResponseToGetSchedules> getAllSchedules();
    List<ResponseToGetSchedules> getScheduleByDate(LocalDate startDate, LocalDate endDate);
    List<ResponseToGetSchedules> getScheduleBySearch(String word);
    SimpleResponse savePatternTimeSheet(PatternTimeSheetRequest request);
    SimpleResponse deleteTimeSheetByDoctorIdAndDate(Long doctorId, LocalDate date, List<TimeSheetDeleteRequest> request);
}