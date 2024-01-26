package healthcheck.service;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.enums.Facility;

public interface ScheduleService {
    SimpleResponse saveAppointment(Facility facility, Long doctorId, AddScheduleRequest addScheduleRequest);
}