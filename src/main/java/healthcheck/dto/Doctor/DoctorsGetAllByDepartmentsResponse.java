package healthcheck.dto.Doctor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorsGetAllByDepartmentsResponse {
    private Long id;
    private String image;
    private String fullName;
    private String department;

}
