package healthcheck.dto.Authentication;

import healthcheck.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignInRequest {
    @Email
    @NotBlank
    private String email;
    @ValidPhoneNumber
    private String password;
}
