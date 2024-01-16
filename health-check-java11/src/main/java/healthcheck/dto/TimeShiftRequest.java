package healthcheck.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeShiftRequest {
    LocalDate dateConsultation;
    LocalTime startTimeConsultation;
    LocalTime endTimeConsultation;
}
