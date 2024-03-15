package healthcheck.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleResponse {
  
    private String messageCode;
    private HttpStatus httpStatus;


    public SimpleResponse(HttpStatus httpStatus, String successMessage) {
        this.httpStatus=httpStatus;
        this.messageCode=successMessage;
    }


    public SimpleResponse(boolean b, String verificationCodeCheckedSuccessfully) {
    }
}