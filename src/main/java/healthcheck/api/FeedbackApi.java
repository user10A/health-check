package healthcheck.api;

import healthcheck.dto.Feedback.FeedbackRequest;
import healthcheck.dto.Feedback.FeedbackResponse;
import healthcheck.dto.Feedback.FeedbackUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
@CrossOrigin
@Tag(name = "Feedback Api", description = "Feedback Endpoints")
public class FeedbackApi {
    private final FeedbackService feedbackService;
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse addFeedback(@RequestBody FeedbackRequest request) {
        return feedbackService.add(request);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse updateFeedback(@RequestBody FeedbackUpdateRequest request) {
        return feedbackService.update(request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse deleteFeedback(@PathVariable("id") Long id) {
        return feedbackService.delete(id);
    }

    @DeleteMapping("/deleteUser/{feedbackId}")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse deleteUserFeedback(@PathVariable("feedbackId") Long feedbackId) {
        return feedbackService.deleteUser(feedbackId);
    }

    @GetMapping("/{doctorId}")
//    @PreAuthorize("hasAuthority('USER')")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public FeedbackResponse getFeedbackByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return feedbackService.getFeedbackByDoctorId(doctorId);
    }
}

