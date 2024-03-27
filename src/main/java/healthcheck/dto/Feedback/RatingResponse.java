package healthcheck.dto.Feedback;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RatingResponse {
    double averageRating;
    int countRating;
}
