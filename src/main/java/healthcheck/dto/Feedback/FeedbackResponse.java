package healthcheck.dto.Feedback;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedbackResponse {
    private Long doctorId;
    private String image;
    private String fullName;
    private String department;
    private int averageRating;
    private List<FeedbackDao>feedbacks;
}
