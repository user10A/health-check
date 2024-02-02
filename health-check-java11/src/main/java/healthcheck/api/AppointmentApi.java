package healthcheck.api;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentResponse> getAppointment(String word){
        return appointmentService.getAllAppointment(word);
    }

    @GetMapping("/confirmation")
    @Operation(summary = "Appointment Confirmation Email", description = "This API is used to send an appointment confirmation email. Requires USER authority.")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse appointmentConfirmationEmail() {
        return appointmentService.appointmentConfirmationEmail();
    }
}