package healthcheck.dto.Authentication;

import healthcheck.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private String email;
    private Role role;
}
