package healthcheck.dto.Appointment;

import lombok.Data;

@Data
public class AppointmentDeleteRequest {
    private Long id;
    private boolean active;
}
