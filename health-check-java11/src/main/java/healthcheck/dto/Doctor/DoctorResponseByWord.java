package healthcheck.dto.Doctor;

import healthcheck.entities.Department;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DoctorResponseByWord {
    private Long id;
    private String image;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private Department department;
    private LocalDate endDateWork;


    public DoctorResponseByWord(Long id, String image, Boolean isActive, String firstName, String lastName, Department department, LocalDate endDateWork) {
        this.id = id;
        this.image = image;
        this.isActive = isActive;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.endDateWork = endDateWork;
    }
}