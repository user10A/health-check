package healthcheck.dto.OnlineAppointment;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class OnlineAppointmentRequest {
    private String name;
    private String numberPhone;
    private String email;
    private String facility;
    private LocalDate date;
    private LocalTime time;
    private boolean inProcessed;
    private Long doctorId;
}