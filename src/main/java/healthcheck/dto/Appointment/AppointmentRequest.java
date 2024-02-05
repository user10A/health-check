package healthcheck.dto.Appointment;

import healthcheck.validation.EmailValidation;
import healthcheck.validation.ValidPhoneNumber;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentRequest {
    Long doctorId;
    String date;
    String startTimeConsultation;
    String fullName;
    @ValidPhoneNumber(message = "Неверный номер телефона!")
    String phoneNumber;
    @EmailValidation(message = "Не верный фортмат почты")
    String email;

}
