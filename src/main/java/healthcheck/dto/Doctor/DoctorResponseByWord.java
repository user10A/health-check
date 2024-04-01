package healthcheck.dto.Doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseByWord {
    private Long id;
    private String position;
    private String image;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private String department;
    private String endDateWork;
    private Timestamp creationDate;
}