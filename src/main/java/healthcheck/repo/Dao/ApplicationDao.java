package healthcheck.repo.Dao;

import healthcheck.dto.Application.ApplicationResponse;

import java.util.List;

public interface ApplicationDao {

    List<ApplicationResponse> getApplications(String word);
    List<ApplicationResponse>getAllApplications();
}
