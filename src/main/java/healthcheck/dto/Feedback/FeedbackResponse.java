package healthcheck.dto.Feedback;

import healthcheck.repo.Dao.FeedbackDao;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedbackResponse {
    private Long doctorId;
    private double averageRating;
    private int count;
    private String image;
    private String fullName;
    private String department;
    private List<FeedbackDaoResponse>feedbacks;
}
