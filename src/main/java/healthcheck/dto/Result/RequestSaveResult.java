package healthcheck.dto.Result;

import healthcheck.enums.Facility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class RequestSaveResult {
    private Long UserId;
    @NotBlank
    @NotNull
    private String url;
    @NotBlank
    @NotNull
    private Facility facility;
    @NotBlank
    @NotNull
    private LocalDate dataOfDelivery;
}
