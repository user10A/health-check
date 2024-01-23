package healthcheck.dto.User;
import healthcheck.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String image;
    private Status status;
    private String surname;
    private String department;

    public UserResponse(Long id, LocalDate appointmentDate, LocalTime appointmentTime, String image, Status status, String surname, String department) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.image = image;
        this.status = status;
        this.surname = surname;
        this.department = department;
    }
}
