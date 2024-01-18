package healthcheck.dto.User;

import healthcheck.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ProfileRequest {
    private String firstName;
    private String lastName;
    @Email
    private String email;
    @ValidPhoneNumber
    private String numberPhone;
}
