package healthcheck.dto.Schedule;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
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


    public ResponseToGetSchedules(String image, String surname, String position, String dayOfWeek, LocalDate dateOfConsultation, LocalTime startTimeOfConsultation, LocalTime endTimeOfConsultation, Boolean isWorkingDay, Boolean availableTime) {
        this.image = image;
        this.surname = surname;
        this.position = position;
        this.dayOfWeek = dayOfWeek;
        this.dateOfConsultation = dateOfConsultation;
        this.startTimeOfConsultation = startTimeOfConsultation;
        this.endTimeOfConsultation = endTimeOfConsultation;
        this.isWorkingDay = isWorkingDay;
        this.availableTime = availableTime;
    }

    public ResponseToGetSchedules() {

    }
}
