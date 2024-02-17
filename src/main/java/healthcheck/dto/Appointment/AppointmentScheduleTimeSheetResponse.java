package healthcheck.dto.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AppointmentScheduleTimeSheetResponse {
    private LocalDate dateOfConsultation;
    private String dayOfWeek;
    private List<String> startTimeOfConsultation;
}
