package healthcheck.service.Impl;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Application;
import healthcheck.repo.ApplicationRepo;
import healthcheck.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepo applicationRepo;

    @Override
    public SimpleResponse createApplication(ApplicationRequest applicationRequest) {
        Application application = Application.builder().username(applicationRequest.getUsername())
                .dateOfApplicationCreation(LocalDate.now())
                .phoneNumber(applicationRequest.getPhoneNumber())
                .processed(false)
                .build();

      try {
            applicationRepo.save(application);
            String successMessage = "Успешно сохранен!";
            log.info(successMessage);
            return new SimpleResponse(HttpStatus.OK, successMessage);
      } catch (Exception e) {
            String errorMessage = "Ошибка при сохранении заявки: " + e.getMessage();
            log.info(errorMessage);
            return SimpleResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("Произошла ошибка.").build();
        }
    }
}