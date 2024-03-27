package healthcheck.dto.Doctor;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoctorSaveRequest {
        private String department;
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @NotBlank
        private String position;
        @NotBlank
        private String image;
        @NotBlank
        private String description;
}