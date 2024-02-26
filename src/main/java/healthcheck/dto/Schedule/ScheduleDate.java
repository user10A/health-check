package healthcheck.dto.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ScheduleDate {
    private LocalDate dateOfConsultation;
    private String dayOfWeek;
    private List<String> startTimeOfConsultation;
}
