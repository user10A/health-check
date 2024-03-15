package healthcheck.dto.Appointment;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponseById {
    private Long id;
    private String doctorImage;
    private String dayOfWeek;
    private LocalDate localDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String doctorFullName;
    private String facility;
}
