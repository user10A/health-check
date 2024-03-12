package healthcheck.dto.TimeSheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetResponse {
    Long doctorId;
    String imageDoctor;
    String department;
    String doctorFullName;
    String dateOfConsultation;
    String dayOfWeek;
    List<String> startTimeOfConsultation;
}
