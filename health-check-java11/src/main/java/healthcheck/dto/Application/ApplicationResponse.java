package healthcheck.dto.Application;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationResponse {
    private long id;
    private String userName;
    private LocalDate date;
    private String number;
    private boolean inProcessed;
}