package healthcheck.service;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.Schedule.ScheduleGetResponse;
import healthcheck.dto.Schedule.ScheduleUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Schedule;
import healthcheck.enums.Facility;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    SimpleResponse saveSchedule(Facility facility, Long doctorId, AddScheduleRequest addScheduleRequest);
    ScheduleGetResponse updateScheduleByDoctorId(Long doctorId, LocalDate date, List<ScheduleUpdateRequest> timeSlots);
    List<ResponseToGetSchedules> getAllSchedules();

    List<ResponseToGetSchedules> getScheduleByDate(LocalDate startDate, LocalDate endDate);

    List<ResponseToGetSchedules> getScheduleBySearch(String word,LocalDate startDate, LocalDate endDate);
    List<Schedule> exportCustomerToExcel(HttpServletResponse response) throws IOException;
}