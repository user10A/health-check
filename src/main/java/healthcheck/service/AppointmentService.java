package healthcheck.service;

import healthcheck.dto.Appointment.*;
import healthcheck.dto.SimpleResponse;
import healthcheck.enums.Facility;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface AppointmentService {
    List<AppointmentResponse> getAllAppointment(String word);
    OnlineAppointmentResponse addAppointment(Facility facility, AppointmentRequest request) throws MessagingException, IOException;
    OnlineAppointmentResponse addAppointmentByDoctorId(AppointmentRequest request) throws MessagingException, IOException;
    SimpleResponse verifyAppointment(Long appointmentId, String verificationCode);
    SimpleResponse deleteAppointment(Long id);
    FindDoctorForAppointmentResponse findByDoctorId(Long id);
    List<AppointmentScheduleTimeSheetResponse> getTheDoctorFreeTimeInTheCalendar(String startDate, String endDate, Long doctorId);
    SimpleResponse appointmentConfirmationEmail(Long id);
    List<AppointmentResponse> getAllAppointmentDefault();
    SimpleResponse deleteAppointmentById(Long id);
    boolean updateProcessed (AppointmentProcessedRequest request);
    SimpleResponse deleteAllAppointmentsById(List<Long> listId);

}