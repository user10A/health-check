package healthcheck.dto.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PatternTimeSheetRequest {
    private Long doctorId;
    private LocalDate dateOfConsultation;
}