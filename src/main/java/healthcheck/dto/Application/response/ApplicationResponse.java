package healthcheck.dto.Application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private long id;
    private String username;
    private LocalDate dateOfApplicationCreation;
    private String phoneNumber;
    private boolean processed;


}
