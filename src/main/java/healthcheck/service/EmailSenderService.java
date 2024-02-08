package healthcheck.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public interface EmailSenderService {
    void sendEmail(String to, String subject, String templateName, Context context);
}