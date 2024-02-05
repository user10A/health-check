package healthcheck.service;

import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.dto.SimpleResponse;

import java.util.List;

public interface ApplicationService {
    SimpleResponse createApplication(ApplicationRequest applicationRequest);
    List<ApplicationResponse> getApplications(String word);
}