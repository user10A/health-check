package healthcheck.dto.Appointment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindDoctorForAppointmentResponse {
    String image;
    String fullNameDoctor;
    String facility;
}
