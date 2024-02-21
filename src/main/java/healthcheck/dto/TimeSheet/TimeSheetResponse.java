package healthcheck.dto.TimeSheet;

import lombok.Builder;
import lombok.Data;
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
