package healthcheck.dto.Application;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationResponse {
    private long id;
    private String username;
    private LocalDate dateOfApplicationCreation;
    private String phoneNumber;
    private boolean processed;
}
