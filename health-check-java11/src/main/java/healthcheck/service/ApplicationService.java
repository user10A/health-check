package healthcheck.service;

import healthcheck.dto.Application.ApplicationRequest;

public interface ApplicationService {
    void createApplication(ApplicationRequest applicationRequest);
}