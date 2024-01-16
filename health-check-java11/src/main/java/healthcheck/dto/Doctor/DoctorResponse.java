package healthcheck.dto.Doctor;

import demo.entities.Department;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String image;
    private Department department;
    private String post;
    private boolean working;
    private LocalDate localDate;
}