package healthcheck.dto.User;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private String password;
}