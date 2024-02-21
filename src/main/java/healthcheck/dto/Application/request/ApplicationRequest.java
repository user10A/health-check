package healthcheck.dto.Application.request;

import healthcheck.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationRequest {
    @NotBlank
    private String username;
    @ValidPhoneNumber
    private String phoneNumber;
}
