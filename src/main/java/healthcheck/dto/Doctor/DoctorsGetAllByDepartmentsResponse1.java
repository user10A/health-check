package healthcheck.dto.Doctor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorsGetAllByDepartmentsResponse1 {
    private Long id;
    private String image;
    private String fullName;
    private String position;
}
