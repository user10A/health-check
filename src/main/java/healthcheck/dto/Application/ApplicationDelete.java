package healthcheck.dto.Application;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDelete {
    private Boolean isActive;
    @NotNull
    private Long id;
}
