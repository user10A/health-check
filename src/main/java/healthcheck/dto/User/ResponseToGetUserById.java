package healthcheck.dto.User;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class ResponseToGetUserById {
    private Long id;
    private String fullName;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
}
