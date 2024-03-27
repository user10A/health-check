package healthcheck.service;

import healthcheck.dto.Feedback.FeedbackDaoResponse;
import healthcheck.dto.Feedback.FeedbackRequest;
import healthcheck.dto.Feedback.FeedbackResponse;
import healthcheck.dto.Feedback.FeedbackUpdateRequest;
import healthcheck.dto.SimpleResponse;

public interface FeedbackService {
    SimpleResponse add(FeedbackRequest request);
    SimpleResponse update(FeedbackUpdateRequest request);
    SimpleResponse delete(Long id);
    SimpleResponse deleteUser(Long id);
    FeedbackResponse getFeedbackByDoctorId(Long id);
    FeedbackDaoResponse getFeedbackById(Long id);

}
