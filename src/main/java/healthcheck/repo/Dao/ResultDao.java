package healthcheck.repo.Dao;

import healthcheck.dto.Result.ResultsUserResponse;

import java.util.List;

public interface ResultDao {
    List<ResultsUserResponse> getAllResultsByUserId(Long id);
}