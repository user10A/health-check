package healthcheck.service.Impl;

import healthcheck.dto.Feedback.FeedbackDaoResponse;
import healthcheck.dto.Feedback.FeedbackRequest;
import healthcheck.dto.Feedback.FeedbackResponse;
import healthcheck.dto.Feedback.FeedbackUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Doctor;
import healthcheck.entities.Feedback;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.FeedbackDao;
import healthcheck.repo.DoctorRepo;
import healthcheck.repo.FeedbackRepo;
import healthcheck.repo.UserAccountRepo;
import healthcheck.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepo feedbackRepo;
    private final DoctorRepo doctorRepo;
    private final UserAccountRepo userAccountRepo;
    private final MessageSource messageSource;
    private final FeedbackDao feedbackDao;

    @Override
    public SimpleResponse add(FeedbackRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info(email);

        UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);
        log.info("User account found: " + userAccount);

        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("error.doctor_not_found",
                        new Object[]{request.getDoctorId()}));
        log.info("Doctor found: " + doctor);
        int rating = request.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("error.illegal_argument_exception.rating");
        }
        Feedback feedback = Feedback.builder()
                .user(userAccount.getUser())
                .doctor(doctor)
                .rating(request.getRating())
                .comment(request.getComment())
                .localDate(LocalDate.now())
                .build();
        feedbackRepo.save(feedback);
        return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("feedback.response.save",
                null, LocaleContextHolder.getLocale()));
    }

    @Override
    public SimpleResponse update(FeedbackUpdateRequest request) {
        int rating = request.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("error.illegal_argument_exception.rating");
        }
        Feedback feedback =feedbackRepo.findById(request.getFeedbackId()).orElseThrow(()-> new NotFoundException("error.feedback.not_found"));
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setLocalDate(LocalDate.now());
        feedbackRepo.save(feedback);
        return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("message.save_response",
                null, LocaleContextHolder.getLocale()));
    }

    @Override
    public SimpleResponse delete(Long id) {
        Feedback feedback =feedbackRepo.findById(id).orElseThrow(()-> new NotFoundException("error.feedback.not_found"));
        feedbackRepo.delete(feedback);
        return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("message.delete_response",
                null, LocaleContextHolder.getLocale()));
    }

    @Override
    public SimpleResponse deleteUser(Long feedBackId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info(email);

        UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);
        log.info("User account found: " + userAccount);
        Feedback feedback = feedbackRepo.findById(feedBackId).orElseThrow(()-> new NotFoundException("error.feedback.not_found"));
        if (!userAccount.getUser().getFeedbacks().contains(feedback)) {
            log.info("User пытается удалить чужой отзыв: %s" + feedback);
            throw new NotFoundException("error.user.feedback.not_found");
        }
        feedbackRepo.delete(feedback);
        log.info("Отзыв удален: %s" + feedback);

        return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("message.delete_response",
                null, LocaleContextHolder.getLocale()));
    }

    @Override
    public FeedbackResponse getFeedbackByDoctorId(Long id) {
        return feedbackDao.getFeedbackByDoctorId(id);
    }

    @Override
    public FeedbackDaoResponse getFeedbackById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info(email);
        UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);
        Feedback feedback =feedbackRepo.findById(id).orElseThrow(()-> new NotFoundException("error.feedback.not_found"));
        return FeedbackDaoResponse.builder()
                .feedbackId(feedback.getId())
                .userFullName(userAccount.getUser().getFirstName()+" "+userAccount.getUser().getLastName())
                .localDate(feedback.getLocalDate())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .build();
    }
}
