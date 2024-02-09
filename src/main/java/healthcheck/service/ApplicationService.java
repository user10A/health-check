package healthcheck.service;

import healthcheck.dto.Application.ApplicationProcessed;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.dto.SimpleResponse;

import java.util.List;

public interface ApplicationService {
    SimpleResponse createApplication(ApplicationRequest applicationRequest);
    List<ApplicationResponse> getApplications(String word);
    SimpleResponse deleteAll(List<Long> request);
    boolean processedById(ApplicationProcessed request);
    SimpleResponse deleteById(Long request);
    List<ApplicationResponse>getAllApplications();
}