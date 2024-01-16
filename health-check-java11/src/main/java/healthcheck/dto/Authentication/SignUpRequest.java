package healthcheck.dto.Authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {
    private String firstNameUp;
    private String lastNameUp;
    private String numberUp;
    private String email;
    private String password;
    private String passwordRep;
}