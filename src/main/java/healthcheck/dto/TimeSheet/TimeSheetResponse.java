package healthcheck.dto.TimeSheet;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class TimeSheetResponse {
    Long doctorId;
    String imageDoctor;
    String doctorFullName;
    String dateOfConsultation;
    String dayOfWeek;
    List<String> startTimeOfConsultation;
}
