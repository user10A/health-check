package healthcheck.dto.User;
import healthcheck.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ResponseToGetAppointmentByUserId {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Status status;
    private String surnameOfDoctor;
    private String department;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private String image;

    public ResponseToGetAppointmentByUserId(LocalDate appointmentDate, LocalTime appointmentTime, Status status, String surnameOfDoctor, String department, String first_name, String last_name, String phone_number, String email, String image) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.surnameOfDoctor = surnameOfDoctor;
        this.department = department;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.email = email;
        this.image = image;
    }


}

