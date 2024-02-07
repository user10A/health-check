package healthcheck.service.Impl;
import healthcheck.dto.Application.ApplicationDelete;
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
import java.util.stream.Collectors;

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
    public SimpleResponse deleteAllPressed(ApplicationDelete request) {
        if (request.getIsActive()) {
            Application application = applicationRepo.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("Application not found with ID: " + request.getId()));
            applicationRepo.delete(application);

            return new SimpleResponse("Successfully deleted", HttpStatus.OK);
        } else {
            return new SimpleResponse("Deletion not performed as isActive is false", HttpStatus.OK);
        }
    }

    @Override
    public List<ApplicationResponse> getAllApplications() {
        return applicationRepo.getAllApplications();
    }

    @Override
    public SimpleResponse deleteAll(List<ApplicationDelete> request) {
        List<Long> idsToDelete = request.stream()
                .filter(ApplicationDelete::getIsActive)
                .map(ApplicationDelete::getId)
                .collect(Collectors.toList());
        try {
            List<Application> applicationsToDelete = applicationRepo.findAllById(idsToDelete);
            applicationRepo.deleteAll(applicationsToDelete);
            return new SimpleResponse("Successfully deleted applications", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new SimpleResponse("Error deleting applications: Some applications not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new SimpleResponse("Error deleting applications: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}