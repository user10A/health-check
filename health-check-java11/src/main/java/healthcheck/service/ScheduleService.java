package healthcheck.service;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ScheduleGetResponse;
import healthcheck.dto.Schedule.ScheduleUpdateRequest;
import java.time.LocalDate;
import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.SimpleResponse;
import healthcheck.enums.Facility;

import java.util.List;

public interface ScheduleService {
     SimpleResponse saveSchedule(Facility facility, Long doctorId, AddScheduleRequest addScheduleRequest);
    ScheduleGetResponse updateScheduleByDoctorId(Long doctorId, LocalDate date, List<ScheduleUpdateRequest.TimeSlot> timeSlots);
    List<ResponseToGetSchedules> getAllSchedules();
    List<ResponseToGetSchedules> getScheduleByDate(String startDate, String endDate);
    List<ResponseToGetSchedules> getScheduleBySearch(String word);

}