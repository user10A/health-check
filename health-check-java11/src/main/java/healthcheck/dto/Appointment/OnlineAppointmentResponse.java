package healthcheck.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class OnlineAppointmentResponse {
    Long id;
    String dayOfWeek;
    LocalDate dateOfAppointment;
    LocalTime startTimeOfConsultation;
    LocalTime endTimeOfConsultation;
    String imageDoctors;
    String fullNameDoctors;
    String facility;
    String verificationCode;
}
