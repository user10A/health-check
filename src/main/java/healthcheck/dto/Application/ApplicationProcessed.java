package healthcheck.dto.Application;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationProcessed {
    private Boolean isActive;
    @NotNull
    private Long id;
}
