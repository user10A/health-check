package healthcheck.email;

import healthcheck.dto.SimpleResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
public interface EmailService {
    SimpleResponse forgotPassword(String email, String link) throws MessagingException, IOException;
    ResponseEntity<String> passwordRecovery(String token,String newPassword);
    SimpleResponse sendMassage(String email, String link,String subject) throws MessagingException, IOException;

}
