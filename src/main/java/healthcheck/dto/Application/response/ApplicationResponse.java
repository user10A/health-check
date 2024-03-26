package healthcheck.dto.Application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {
    private long id;
    private String username;
    private LocalDate dateOfApplicationCreation;
    private String phoneNumber;
    private boolean processed;
    private Timestamp creationDate;


    public ApplicationResponse(long i, String johnDoe, LocalDate now, String number, boolean b) {
    }
}