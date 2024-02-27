package healthcheck.dto.Application.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProcessedRequest {
    private Boolean isActive;
    @NotNull
    private Long id;


}
