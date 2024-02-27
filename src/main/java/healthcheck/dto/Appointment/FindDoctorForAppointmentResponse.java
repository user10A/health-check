package healthcheck.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindDoctorForAppointmentResponse {
    String image;
    String fullNameDoctor;
    String facility;
}
