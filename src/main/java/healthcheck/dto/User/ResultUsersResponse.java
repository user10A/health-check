package healthcheck.dto.User;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
public class ResultUsersResponse {
    private Long id;
    private String surname;
    private String phoneNumber;
    private String email;
    private LocalDate resultDate;

    public ResultUsersResponse(Long id, String surname, String phoneNumber, String email, LocalDate resultDate) {
        this.id = id;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.resultDate = resultDate;
    }
}
