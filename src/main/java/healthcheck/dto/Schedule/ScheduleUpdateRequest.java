package healthcheck.dto.Schedule;
import lombok.Data;

@Data
public class ScheduleUpdateRequest {
    private String fromTime;
    private String toTime;

}

