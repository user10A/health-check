package healthcheck.dto.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToGetSchedules {
    private Long id;
    private String image;
    private String surname;
    private String position;
    private List<ScheduleDate> dates;
    private LocalDateTime creationDate;
}