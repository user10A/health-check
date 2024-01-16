package healthcheck.dto.OnlineAppointment;

import demo.entities.Department;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class OnlineAppointmentResponse {
    private Long id;
    private String name;
    private String numberPhone;
    private String email;
    private Department department;
    private LocalDate date;
    private LocalTime time;
    private boolean inProcessed;
}
