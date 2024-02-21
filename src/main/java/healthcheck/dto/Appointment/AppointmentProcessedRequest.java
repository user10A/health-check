package healthcheck.dto.Appointment;

import lombok.Data;

@Data
public class AppointmentProcessedRequest { // o
    private Long id;
    private boolean active;
}
