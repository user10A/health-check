package healthcheck.service;

import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.SimpleResponse;

public interface ApplicationService {
    SimpleResponse createApplication(ApplicationRequest applicationRequest);
}