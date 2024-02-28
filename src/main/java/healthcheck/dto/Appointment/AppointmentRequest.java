package healthcheck.dto.Appointment;

import healthcheck.validation.EmailValidation;
import healthcheck.validation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    Long doctorId;
    String date;
    String startTimeConsultation;
    String fullName;
    @ValidPhoneNumber(message = "Неверный номер телефона!")
    String phoneNumber;
    @EmailValidation(message = "Неверный формат почты")
    String email;


}
