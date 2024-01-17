package healthcheck.service.Impl;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.entities.Application;
import healthcheck.repo.ApplicationRepo;
import healthcheck.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepo applicationRepo;

    @Override
    public void createApplication(ApplicationRequest applicationRequest) {
        Application application = Application.builder().username(applicationRequest.getUsername())
                .dateOfApplicationCreation(LocalDate.now())
                .phoneNumber(applicationRequest.getPhoneNumber())
                .processed(false)
                .build();

        applicationRepo.save(application);
    }
}