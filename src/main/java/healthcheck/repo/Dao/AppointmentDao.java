package healthcheck.repo.Dao;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.Appointment.AppointmentScheduleTimeSheetResponse;

import java.util.List;

public interface AppointmentDao {

    List<AppointmentScheduleTimeSheetResponse> getTheDoctorFreeTimeInTheCalendar(String startDate, String endDate, Long doctorId);
    List<AppointmentResponse> getAllAppointment(String word);

    List<AppointmentResponse> getAllAppointmentDefault();
}
