package healthcheck.dto.deparment;

import demo.dto.Doctor.DoctorResponse;
import demo.enums.Facility;

import java.util.List;

public class DepartmentResponse {
    private Facility facility;
    private List<DoctorResponse> department;
}
