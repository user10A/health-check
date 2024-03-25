package healthcheck.dto.Feedback;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
@Builder
@Data
public class FeedbackDao {
    private Long feedbackId;
    private String userImage;
    private String userFullName;
    private LocalDate localDate;
    private int rating;
    private String comment;

}
