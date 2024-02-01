package healthcheck.dto.Schedule;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleUpdateRequest {
    private List<TimeSlot> timeSlots;

    @Data
    public static class TimeSlot {
        private String fromTime;
        private String toTime;
    }
}
