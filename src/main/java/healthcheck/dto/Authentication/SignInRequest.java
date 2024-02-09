package healthcheck.dto.Authentication;

import healthcheck.validation.EmailValidation;
import healthcheck.validation.ValidPassword;
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
    @EmailValidation(message = "Email не может быть пустым и должен быть валидным")
    private String email;
    @ValidPassword
    private String password;
}
