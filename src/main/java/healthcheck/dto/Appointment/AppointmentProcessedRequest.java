package healthcheck.dto.Appointment;

import lombok.Data;

@Data
public class AppointmentProcessedRequest {
    private Long id;
    private boolean active;
}
