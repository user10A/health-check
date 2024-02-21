package healthcheck.dto.User;
import healthcheck.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ResponseToGetUserAppointments {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String image;
    private Status status;
    private String surname;
    private String department;
}
