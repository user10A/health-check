package healthcheck.dto.Authentication;

import healthcheck.validation.EmailValidation;
import healthcheck.validation.ValidPassword;
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
    @ValidPhoneNumber(message = "Неверный формат номера телефона")
    private String number;
    @EmailValidation(message = "Неверный формат почты")
    private String email;
    @ValidPassword
    private String password;
}