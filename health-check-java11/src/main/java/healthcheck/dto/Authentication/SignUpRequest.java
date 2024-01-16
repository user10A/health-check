package healthcheck.dto.Authentication;

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
    private String email;
    private String password;
}