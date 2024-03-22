package healthcheck.service.Impl;

import healthcheck.dto.Application.request.ApplicationProcessedRequest;
import healthcheck.dto.Application.request.ApplicationRequest;
import healthcheck.dto.Application.response.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Application;
import healthcheck.exceptions.AlreadyExistsException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.ApplicationRepo;
import healthcheck.repo.Dao.ApplicationDao;
import healthcheck.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepo applicationRepo;
    private final ApplicationDao applicationDao;
    private final MessageSource messageSource;
    @Override
    @Transactional
    public SimpleResponse createApplication(ApplicationRequest applicationRequest) {
        if (applicationRepo.existsByPhoneNumber(applicationRequest.getPhoneNumber())){
            throw new AlreadyExistsException("alreadyExists.phoneNumber",new Object[]{applicationRequest.getPhoneNumber()});
        }
        Application application = Application.builder()
                        .username(applicationRequest.getUsername())
                        .dateOfApplicationCreation(LocalDate.now())
                        .phoneNumber(applicationRequest.getPhoneNumber())
                        .processed(false)
                        .build();
    try {
         applicationRepo.save(application);
         return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("application.response",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.info("Ошибка при сохранении заявки: " + e.getMessage());
            return new SimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR,messageSource.getMessage("application.saveErrorResponse",null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public List<ApplicationResponse> getApplications(String word) {
        return applicationDao.getApplications(word);
    }

    @Override
    public List<ApplicationResponse> getAllApplications() {
        return applicationDao.getAllApplications();
    }

    @Transactional
    @Override
    public SimpleResponse deleteAll(List<Long> request) {
        try {
            log.info("list id in methods delete all : {}", request);
            List<Application> applicationsToDelete = applicationRepo.findAllById(request);
            log.info("applications found");
            applicationRepo.deleteAll(applicationsToDelete);
            log.info("Successfully deleted applications");
            return new SimpleResponse(HttpStatus.OK,messageSource.getMessage("application.deleteResponse",null, LocaleContextHolder.getLocale()));
        } catch (EmptyResultDataAccessException e) {
            return new SimpleResponse(HttpStatus.NOT_FOUND, messageSource.getMessage("application.deleteErrorResponse",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            return new SimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR,messageSource.getMessage("application.deleteErrorResponse1",null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    @Transactional
    public boolean processedById(ApplicationProcessedRequest request) {
        try {
            Optional<Application> applicationOptional = applicationRepo.findById(request.getId());
            Application application = applicationOptional.orElseThrow(() -> new NotFoundException("application.deleteNotFound", new Object[]{request.getId()}));
            log.info("Заявка найдена по ID: " + request.getId());
            application.setProcessed(request.getIsActive());
            log.info("Заявка успешно обновлена, статус обработки: " + application.isProcessed());
            applicationRepo.save(application);
            return application.isProcessed();
        } catch (NotFoundException e) {
            log.error("Ошибка обработки заявки: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public SimpleResponse deleteById(Long request) {
        Application application = applicationRepo.findById(request)
                .orElseThrow(() -> new NotFoundException("application.deleteNotFound",new Object[]{request}));
        if (application.isProcessed()) {
            log.info("Application found by id: " + request);
            applicationRepo.delete(application);
            log.info("Successfully deleted application");
            return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("application.deleteResponse", null, LocaleContextHolder.getLocale()));
        } else {
            log.error("Application with ID: " + request + " is not processed, cannot delete.");
            return new SimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR, messageSource.getMessage("application.deleteErrorResponse1", null, LocaleContextHolder.getLocale()));
        }
    }
}