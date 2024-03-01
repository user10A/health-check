package healthcheck.dto.Result;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ResultsUserResponse {
    private String Facility;
    private LocalDate date;
    private LocalTime localTime;
    private String numberResult;
    private String pdf;
}
