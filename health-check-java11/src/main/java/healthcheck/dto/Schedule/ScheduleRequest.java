package healthcheck.dto.Schedule;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long doctorId;
    private Long scheduleWorkingDayId;
}