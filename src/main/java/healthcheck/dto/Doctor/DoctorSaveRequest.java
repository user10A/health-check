package healthcheck.dto.Doctor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoctorSaveRequest {
        private Long departmentId;
        @NotBlank
        @NotNull
        private String firstName;
        @NotBlank
        @NotNull
        private String lastName;
        @NotBlank
        @NotNull
        private String position;
        @NotBlank
        @NotNull
        private String image;
        @NotBlank
        @NotNull
        private String description;
}