package healthcheck.dto.User;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String number;
    private String email;
}