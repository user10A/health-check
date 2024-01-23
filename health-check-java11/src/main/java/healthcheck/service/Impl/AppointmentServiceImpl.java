package healthcheck.service.Impl;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.entities.Appointment;
import healthcheck.enums.Status;
import healthcheck.repo.AppointmentRepo;
import healthcheck.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepo;

    @Override
    public List<AppointmentResponse> getAllAppointment(String word) {
        List<Appointment> all = appointmentRepo.getAllAppointment(word);
        List<AppointmentResponse> response = new ArrayList<>();
        boolean status = false;
        for (Appointment appointment : all) {
            if (appointment.getStatus().equals(Status.CONFIRMED)) {
                status = false;
            } else if (appointment.getStatus().equals(Status.FINISHED)) {
                status = true;
            }

            String username = appointment.getUser().getFirstName() + " " +
                    appointment.getUser().getLastName();

            response.add(AppointmentResponse.builder()
                    .appointmentId(appointment.getId())
                    .fullName(username)
                    .phoneNumber(appointment.getUser().getPhoneNumber())
                    .email(appointment.getUser().getUserAccount().getEmail())
                    .facility(String.valueOf(appointment.getDepartment().getFacility()))
                    .specialist(appointment.getDoctor().getFullNameDoctor())
                    .localDate(appointment.getAppointmentDate())
                    .localTime(appointment.getAppointmentTime())
                    .status(status)
                    .build());
        }
        return response;
    }
}
