package healthcheck.dto.User;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class ResponseToGetUserById {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;

    public ResponseToGetUserById(Long id, String first_name, String last_name, String email, String phone_number) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
    }
}
