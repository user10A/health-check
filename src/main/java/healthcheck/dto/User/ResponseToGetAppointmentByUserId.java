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

}

