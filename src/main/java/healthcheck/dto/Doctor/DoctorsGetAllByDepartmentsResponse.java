package healthcheck.dto.Doctor;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DoctorsGetAllByDepartmentsResponse {
    private String department;
    List<DoctorsGetAllByDepartmentsResponse1> doctors;
}
