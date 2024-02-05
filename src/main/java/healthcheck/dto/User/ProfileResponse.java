package healthcheck.dto.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
