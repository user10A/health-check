package healthcheck.service.Impl;

import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.Result.ResultsUserResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Department;
import healthcheck.entities.Result;
import healthcheck.entities.User;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.ResultDao;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.ResultRepo;
import healthcheck.repo.UserRepo;
import healthcheck.service.EmailSenderService;
import healthcheck.service.ResultService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultServiceImpl implements ResultService {
    private final ResultRepo resultRepo;
    private final ResultDao resultDao;
    private final DepartmentRepo departmentRepo;
    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public SimpleResponse saveResult(RequestSaveResult request) {
        try {
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(7);
            Department department = departmentRepo.findByFacility(request.getFacility());
            User user = userRepo.findById(request.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Пользователь с ID: %d не найден", request.getUserId())
                    ));
            Result result = Result.builder()
                    .resultDate(request.getDataOfDelivery())
                    .pdfUrl(request.getUrl())
                    .department(department)
                    .timeOfUploadingResult(LocalTime.now())
                    .resultNumber(generateTenDigitNumber())
                    .user(user)
                    .build();
            if (request.getDataOfDelivery().isEqual(startDate) || (request.getDataOfDelivery().isAfter(startDate) && request.getDataOfDelivery().isBefore(endDate))) {
                resultRepo.save(result);
                log.info("Результат с полным именем пациента: %s успешно добавлен!".formatted(user.getFirstName() + " " + user.getLastName()));
                Context context = new Context();
                context.setVariable("patientName", user.getFirstName() + " " + user.getLastName());
                context.setVariable("departmentName", department.getFacility());
                context.setVariable("generateNumber", result.getResultNumber());
                emailSenderService.sendEmail(user.getUserAccount().getEmail(), "HealthCheck : Оповещение о результате", "result", context);
                log.info("Сообщение отправлено пользователю с email : %s".formatted(user.getUserAccount().getEmail()));
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                String emailContent = templateEngine.process("result", context);
                helper.setTo(user.getUserAccount().getEmail());
                helper.setText(emailContent, true);
                String successMessage = "Успешно сохранен!";
                log.info(successMessage);
                return new SimpleResponse(HttpStatus.OK, successMessage);
            } else {
                log.info("Результат не сохранен! :"+request);
                return new SimpleResponse(HttpStatus.BAD_REQUEST, String.format("%s : Текущая дата находится вне заданного диапазона. Добавление результата в течении 7 дней!", request.getDataOfDelivery()));
            }
        } catch (Exception e) {
            String errorMessage = "Ошибка при сохранении заявки: " + e.getMessage();
            log.error(errorMessage, e);
            return SimpleResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).messageCode("Произошла ошибка.").build();
        }
    }

    @Override
    public String getResultByResultNumberResult(Long resultNumber) {
        String result = resultRepo.getResultByResultNumberResult(resultNumber);
        if (result == null) {
            throw new NotFoundException(String.format("Результат с номером %d не найден", resultNumber));
        }
        log.info("результат найден {}", result);
        return result;
    }

    @Override
    public List<ResultsUserResponse> getAllResultsByUserId(Long id) {
        return resultDao.getAllResultsByUserId(id);
    }


    private static Long generateTenDigitNumber() {
        return (long) (Math.random() * 9000000000L) + 1000000000L;
    }
}