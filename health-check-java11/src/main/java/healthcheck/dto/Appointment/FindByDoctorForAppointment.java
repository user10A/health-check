package healthcheck.dto.Appointment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindByDoctorForAppointment {
    String image;
    String fullNameDoctor;
    String facility;
}
