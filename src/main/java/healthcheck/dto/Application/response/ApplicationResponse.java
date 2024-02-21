package healthcheck.dto.Application.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicationResponse {
    private long id;
    private String username;
    private LocalDate dateOfApplicationCreation;
    private String phoneNumber;
    private boolean processed;

    public ApplicationResponse(long id, String username, LocalDate dateOfApplicationCreation, String phoneNumber, boolean processed) {
        this.id = id;
        this.username = username;
        this.dateOfApplicationCreation = dateOfApplicationCreation;
        this.phoneNumber = phoneNumber;
        this.processed = processed;
    }
}
