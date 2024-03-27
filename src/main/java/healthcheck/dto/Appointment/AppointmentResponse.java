package healthcheck.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private Long appointmentId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String facility;
    private String specialist;
    private LocalDate localDate;
    private LocalTime localTime;
    private String status;
    private boolean processed;
    private Timestamp creationDate;

    public AppointmentResponse(long l, String johnDoe, String number, String mail, String s, String s1, LocalDate now, LocalTime now1, String scheduled, boolean b) {
    }
}