package healthcheck.service;

import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.SimpleResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

public interface ResultService {
    SimpleResponse saveResult(RequestSaveResult request);


}
