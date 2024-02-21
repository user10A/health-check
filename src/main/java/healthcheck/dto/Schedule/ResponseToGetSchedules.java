package healthcheck.dto.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToGetSchedules {
    private String image;
    private String surname;
    private String position;
    private String dayOfWeek;
    private LocalDate dateOfConsultation;
    private LocalTime startTimeOfConsultation;
    private LocalTime endTimeOfConsultation;
    private Boolean isWorkingDay;
    private Boolean availableTime;
}
