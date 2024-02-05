package healthcheck.dto.Doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorUpdateRequest {
    @Size(min = 3, max = 30, message = "Name should be between 2 and 30 characters.")
    @NotNull(message = "Name cannot be empty!")
    String firstName;
    @Size(min = 2, max = 30, message = "Last name should be between 2 and 30 characters.")
    @NotNull(message = "Last name cannot be empty!")
    String lastName;
    @NotBlank(message = "Position cannot be empty!")
    @NotNull(message = "Position cannot be empty!")
    String position;
    @NotBlank(message = "Image cannot be empty!")
    String image;
    @NotNull(message = "Description cannot be empty!")
    String description;

}
