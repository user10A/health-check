package healthcheck.service;

import healthcheck.dto.Application.ApplicationDelete;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.dto.Application.ApplicationResponse;
import healthcheck.dto.SimpleResponse;

import java.util.List;

public interface ApplicationService {
    SimpleResponse createApplication(ApplicationRequest applicationRequest);
    List<ApplicationResponse> getApplications(String word);
    SimpleResponse deleteAll(List<ApplicationDelete> request);
    SimpleResponse deleteAllPressed(ApplicationDelete request);
    List<ApplicationResponse>getAllApplications();
}