package healthcheck.dto.Doctor;

import demo.enums.Facility;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorRequest {
    private String firstName;
    private String lastName;
    private String image;
    private Facility departmentName;
    private String post;
    private boolean working;
    private LocalDate localDate;
}
