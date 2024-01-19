package healthcheck.service;

import healthcheck.dto.Appointment.AppointmentResponse;

import java.util.List;

public interface AppointmentService {
    List<AppointmentResponse> getAllAppointment(String word);
}