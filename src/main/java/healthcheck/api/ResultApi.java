package healthcheck.api;

import healthcheck.S3.S3Service;
import healthcheck.dto.Result.RequestSaveResult;
import healthcheck.dto.Result.ResultsUserResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.jms.JmsService;
import healthcheck.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/result")
@CrossOrigin
@Tag(name = "Result api", description = "API's for results ")
public class ResultApi {

    private final ResultService resultService;
    private final S3Service s3Service;
    private final JmsService jmsService;
    @Value("${activemq.queue.name}")
    private String pdfQueue;


    @PostMapping("/save")
    @Operation(summary = "Save Result", description = "This API is used to save a result.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SimpleResponse> saveResult(@RequestBody RequestSaveResult request) {
        SimpleResponse response = resultService.saveResult(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
    @GetMapping()
    @Operation(summary = "Get result by result Number",description = "This method get result by result number")
    @PreAuthorize("hasAuthority('USER')")
    public String getResult(@RequestParam Long resultNumber){
        String result = resultService.getResultByResultNumberResult(resultNumber);
        jmsService.sendMessage(pdfQueue, result);
        return result;
    }
    @JmsListener(destination = "pdfQueue")
    @GetMapping("/pdf")
    @Operation(summary = "Get PDF file by methods get result",description = "This method get pdf file")
    @PreAuthorize("hasAuthority('USER')")
    public byte[] getPdf(){
        String message= jmsService.receiveMessage("pdfQueue");
        return s3Service.getPdfFileByUrl(message);
    }


}
