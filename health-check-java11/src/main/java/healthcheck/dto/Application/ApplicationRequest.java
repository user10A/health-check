package healthcheck.dto.Application;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationRequest {
    private String userName;
    private LocalDate date;
    private String number;
}