package healthcheck.dto.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserResponseByToken {
    private String firstName;
    private String lastName;
    private String number;
    private String email;
}