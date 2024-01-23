package healthcheck.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultUsersResponse {
    private Long id;
    private String surname;
    private String phoneNumber;
    private String email;
    private LocalDate resultDate;
}
