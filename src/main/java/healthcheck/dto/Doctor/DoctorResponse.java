package healthcheck.dto.Doctor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorResponse {
    private Long id;
    private String image;
    private String firstName;
    private String lastName;
    private String department;
    private String position;
    private String description;
}
