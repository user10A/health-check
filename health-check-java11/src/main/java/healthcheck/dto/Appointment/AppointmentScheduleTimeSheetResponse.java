package healthcheck.dto.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class AppointmentScheduleTimeSheetResponse {
    private LocalDate dateOfConsultation;
    private String dayOfWeek;
    private LocalTime startTimeOfConsultation;
}
