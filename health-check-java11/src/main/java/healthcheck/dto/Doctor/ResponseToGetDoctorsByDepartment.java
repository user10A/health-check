package healthcheck.dto.Doctor;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ResponseToGetDoctorsByDepartment {
    private Long doctorId;
    private String department;
    private String full_name;
    private String image;
    private String position;

    public ResponseToGetDoctorsByDepartment(Long doctorId, String department, String full_name, String image, String position) {
        this.doctorId = doctorId;
        this.department = department;
        this.full_name = full_name;
        this.image = image;
        this.position = position;
    }
}
