package healthcheck.api;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentApi {

    private final AppointmentService appointmentService;

    @GetMapping("/getAppointment")
    @Operation(summary = "Get appointment", description = "Endpoint to get appointment.")
    @PostAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentResponse> getAppointment(String word){
        return appointmentService.getAllAppointment(word);
    }

    @GetMapping("/confirmation")
    @PostAuthorize("hasAnyAuthority('USER')")
    public void buildAppointmentConfirmationEmail() {
        appointmentService.buildAppointmentConfirmationEmail();
    }
}
