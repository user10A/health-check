package healthcheck.dto.Doctor;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DoctorResponseByWord {
    private Long id;
    private String position;
    private String image;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private String department;
    private LocalDate endDateWork;
}