package healthcheck.service;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.SimpleResponse;

import java.util.List;

public interface AppointmentService {
    List<AppointmentResponse> getAllAppointment(String word);
    SimpleResponse appointmentConfirmationEmail();
}