package healthcheck.api;

import healthcheck.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentApi {
    private final AppointmentService appointmentService;
}
