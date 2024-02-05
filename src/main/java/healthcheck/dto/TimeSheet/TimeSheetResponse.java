package healthcheck.dto.TimeSheet;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TimeSheetResponse {
    Long doctorId;
    String imageDoctor;
    String doctorFullName;
    LocalDate dateOfConsultation;
    String dayOfWeek;
    LocalTime startTimeOfConsultation;
}
