package healthcheck.service;

import healthcheck.dto.Application.request.ApplicationProcessedRequest;
import healthcheck.dto.Application.request.ApplicationRequest;
import healthcheck.dto.Application.response.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import java.util.List;

public interface ApplicationService {
    SimpleResponse createApplication(ApplicationRequest applicationRequest);
    List<ApplicationResponse> getApplications(String word);
    SimpleResponse deleteAll(List<Long> request);
    boolean processedById(ApplicationProcessedRequest request);
    SimpleResponse deleteById(Long request);
    List<ApplicationResponse>getAllApplications();
}