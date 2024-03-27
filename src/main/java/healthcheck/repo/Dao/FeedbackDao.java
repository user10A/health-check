package healthcheck.repo.Dao;

import healthcheck.dto.Feedback.FeedbackResponse;
import healthcheck.dto.Feedback.RatingResponse;

public interface FeedbackDao {
    FeedbackResponse getFeedbackByDoctorId(Long id);
    RatingResponse getRaringResponseByIdDoctor(Long id);
}
