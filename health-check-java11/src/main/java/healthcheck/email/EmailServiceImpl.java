package healthcheck.email;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.UserAccountRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${app.verification-code-expiration-minutes}")
    private int verificationCodeExpirationMinutes;
    @Value("${spring.mail.username}")
    private String mailUsername;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final UserAccountRepo userAccountRepo;

    @Override
    public SimpleResponse forgotPassword(String email, String link) throws MessagingException, IOException {
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пациент с таким email: %s не существует!", email)));

        String token = UUID.randomUUID().toString();
        userAccount.setTokenPassword(token);
        userAccount.setVerificationCodeTime(new Date());
        userAccountRepo.save(userAccount);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(mailUsername);
        helper.setSubject("Сброс пароля");
        helper.setTo(email);
        Resource resource = new ClassPathResource("templates/message.html");
        String htmlContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String formattedHtmlContent = htmlContent.replace("%s", link + "/" + token);
        helper.setText(formattedHtmlContent, true);

        javaMailSender.send(mimeMessage);
        log.info("Электронное письмо успешно отправлено на адрес: {}", email);
        return new SimpleResponse(String.format("Ссылка для сброса пароля отправлена пользователю с email : %s", email + " token: " + userAccount.getTokenPassword()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> passwordRecovery(String token,String newPassword) {
        UserAccount userAccount = userAccountRepo.getByUserAccountByTokenPassword(token).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пациент с таким email: %s не существует!", token)));

        if (token.equals(userAccount.getTokenPassword())) {
            if (isVerificationCodeExpired(userAccount)) {
                userAccount.setTokenPassword(null);
                log.info("вермя истекло");
                throw new RuntimeException("Verification code has expired.");
            } else {
                userAccount.setPassword(passwordEncoder.encode(newPassword));
                userAccount.setTokenPassword(null);
                userAccountRepo.save(userAccount);
                log.info("пароль успешно изменен");
            }
        } else {
            throw new RuntimeException("Invalid verification code.");
        }
        return null;
    }
    public boolean isVerificationCodeExpired(UserAccount request) {
        Date currentTime = new Date();
        Date sentTime = request.getVerificationCodeTime();
        long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(currentTime.getTime() - sentTime.getTime());
        return minutesDifference > verificationCodeExpirationMinutes;
    }
}
