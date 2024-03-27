package healthcheck.dto.Feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackRequest {
    private Long doctorId;
    @Min(1)
    @Max(5)
    private int rating;
    @NotBlank()
    private String comment;
}
