package healthcheck.service;

import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.SimpleResponse;

public interface ResultService {
    SimpleResponse saveResult(RequestSaveResult request);
    String getResultByResultNumberResult(Long resultNumber);

public interface ResultService {
    SimpleResponse saveResult(RequestSaveResult request);
}

