package healthcheck.dto.Schedule;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    // Add additional fields if needed
}
