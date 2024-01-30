package healthcheck.service;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.SimpleResponse;
import healthcheck.enums.Facility;

import java.util.List;

public interface ScheduleService {
    SimpleResponse saveAppointment(Facility facility, Long doctorId, AddScheduleRequest addScheduleRequest);
    List<ResponseToGetSchedules> getAllSchedules();
    List<ResponseToGetSchedules> getScheduleByDate(String startDate, String endDate);
    List<ResponseToGetSchedules> getScheduleBySearch(String word);

}