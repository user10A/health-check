package healthcheck.dto.Application.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationProcessedRequest {
    private Boolean isActive;
    @NotNull
    private Long id;
}
