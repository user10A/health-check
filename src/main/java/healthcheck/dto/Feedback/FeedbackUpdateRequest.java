package healthcheck.dto.Feedback;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackUpdateRequest {
    private Long feedbackId;
    @NotBlank()
    private int rating;
    @NotBlank()
    private String comment;
}
