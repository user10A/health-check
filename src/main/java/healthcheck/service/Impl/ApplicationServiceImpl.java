package healthcheck.service.Impl;
import healthcheck.dto.Application.ApplicationProcessed;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Application;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.ApplicationRepo;
import healthcheck.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<ApplicationResponse> getApplications(String word) {
        return applicationRepo.getApplications(word);
    }

    @Override
    public List<ApplicationResponse> getAllApplications() {
        return applicationRepo.getAllApplications();
    }

    @Override
    public SimpleResponse deleteAll(List<Long> request) {
        try {
            log.info("list id in methods delete all : {}",request);
            List<Application> applicationsToDelete = applicationRepo.findAllById(request);
            log.info("applications found");
            applicationRepo.deleteAll(applicationsToDelete);
            log.info("Successfully deleted applications");
            return new SimpleResponse("Successfully deleted applications", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new SimpleResponse("Error deleting applications: Some applications not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new SimpleResponse("Error deleting applications: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean processedById(ApplicationProcessed request) {
        Optional<Application> application = Optional.ofNullable(applicationRepo.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Not found Application by Id: " + request.getId())));
        log.info("Application found by id: "+request.getId());
        application.ifPresent(value -> value.setProcessed(request.getIsActive()));
        log.info("Application successfully update is processed: "+ application.get().isProcessed());
        applicationRepo.save(application.get());
        return application.get().isProcessed();
    }

    @Override
    public SimpleResponse deleteById(Long request) {
        Application application = applicationRepo.findById(request)
                .orElseThrow(() -> new NotFoundException("Application not found with ID: " + request));
        if(application.isProcessed()) {
            log.info("Application found by id: " + request);
            applicationRepo.delete(application);
            log.info("Successfully deleted application");
            return new SimpleResponse("Successfully deleted", HttpStatus.OK);
        } else {
            log.error("Application with ID: " + request + " is not processed, cannot delete.");
            return new SimpleResponse("Error deleting application: Application not processed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

