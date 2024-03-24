package healthcheck.email;

import healthcheck.dto.Authentication.AuthenticationResponse;
import healthcheck.dto.SimpleResponse;
import jakarta.mail.MessagingException;

import java.io.IOException;
public interface EmailService {
    SimpleResponse forgotPassword(String email, String link) throws MessagingException, IOException;
    AuthenticationResponse passwordRecovery(String token, String newPassword) throws Exception;
    SimpleResponse sendMassage(String username,String email,String toEmail, String link,String subject) throws MessagingException, IOException;
    String getGreeting();

}
