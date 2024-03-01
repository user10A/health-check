package healthcheck.service;

import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.Result.ResultsUserResponse;
import healthcheck.dto.SimpleResponse;

import java.util.List;

public interface ResultService {
    SimpleResponse saveResult(RequestSaveResult request);
    String getResultByResultNumberResult(Long resultNumber);
    List<ResultsUserResponse> getAllResultsByUserId(Long id);

}

