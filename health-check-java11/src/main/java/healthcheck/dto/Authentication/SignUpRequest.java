package healthcheck.dto.Authentication;

import healthcheck.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String number;
    @Email(message = "Invalid email format")
    @NotBlank
    private String email;
    @ValidPhoneNumber
    private String password;
}