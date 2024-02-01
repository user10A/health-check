package healthcheck.dto.Schedule;

import healthcheck.entities.Department;
import healthcheck.entities.TimeSheet;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ScheduleGetResponse {
    private Department department;
    private String doctorFullName;
    private LocalDate localDateConsultation;
    private List<TimeSheet> timeSheets;
}
