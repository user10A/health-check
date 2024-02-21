package healthcheck.dto.Result;

import healthcheck.enums.Facility;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class RequestSaveResult {
    private Long UserId;
    @NotBlank
    private String url;
    @NotBlank
    private Facility facility;
    @NotBlank
    private LocalDate dataOfDelivery;
}
