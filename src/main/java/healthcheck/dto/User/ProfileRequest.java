package healthcheck.dto.User;

import healthcheck.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Email
    private String email;
    @ValidPhoneNumber
    private String numberPhone;
}