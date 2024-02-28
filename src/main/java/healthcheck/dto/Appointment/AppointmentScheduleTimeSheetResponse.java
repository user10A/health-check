package healthcheck.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentScheduleTimeSheetResponse {
    private LocalDate dateOfConsultation;
    private String dayOfWeek;
    private List<String> startTimeOfConsultation;



    public AppointmentScheduleTimeSheetResponse(String date, String time, String time1) {
    }
}
