package healthcheck.dto.Application.request;

import healthcheck.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    @NotBlank
    private String username;
    @ValidPhoneNumber
    private String phoneNumber;


}
