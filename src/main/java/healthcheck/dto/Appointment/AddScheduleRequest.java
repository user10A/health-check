package healthcheck.dto.Appointment;

import healthcheck.enums.DaysOfRepetition;
import healthcheck.enums.Interval;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class AddScheduleRequest {
    @FutureOrPresent
    private LocalDate createStartDate;
    @FutureOrPresent
    private LocalDate createEndDate;
    private String startTime;
    private String endTime;
    private Interval interval;
    private String startBreak;
    private String endBreak;
    private Map<DaysOfRepetition, Boolean> dayOfWeek;
}
