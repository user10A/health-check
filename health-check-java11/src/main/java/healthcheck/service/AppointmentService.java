package healthcheck.service;

import healthcheck.dto.Appointment.*;
import healthcheck.dto.SimpleResponse;
import healthcheck.enums.Facility;
import jakarta.mail.MessagingException;

import java.io.IOException;
import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.SimpleResponse;

import java.util.List;

public interface AppointmentService {
    List<AppointmentResponse> getAllAppointment(String word);
    void buildAppointmentConfirmationEmail();
    OnlineAppointmentResponse addAppointment(Facility facility, AppointmentRequest request) throws MessagingException, IOException;
    SimpleResponse verifyAppointment(Long appointmentId, String verificationCode);
    SimpleResponse deleteAppointment(Long id);
    FindByDoctorForAppointment findByDoctorId(Long id);
    List<AppointmentScheduleTimeSheetResponse> getTheDoctorFreeTimeInTheCalendar(String startDate, String endDate, Long doctorId);
    SimpleResponse appointmentConfirmationEmail();
}