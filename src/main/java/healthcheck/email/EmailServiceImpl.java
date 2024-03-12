package healthcheck.email;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import healthcheck.config.JwtService;
import healthcheck.dto.Authentication.AuthenticationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.UserAccountRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalTime;
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
    private final TemplateEngine templateEngine;
    private final JwtService jwtService;

    @Override
    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пациент с таким email: %s не существует!", email)));

        String token = UUID.randomUUID().toString();
        User user = userAccount.getUser();
        userAccount.setTokenPassword(token);
        userAccount.setVerificationCodeTime(new Date());
        userAccountRepo.save(userAccount);
        Context context = new Context();
        context.setVariable("userName", user.getFirstName() + " " + user.getLastName());
        context.setVariable("greeting", getGreeting());
        context.setVariable("link", link);
        String emailContent = templateEngine.process("message", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailUsername);
        helper.setSubject("Сброс пароля");
        helper.setTo(email);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
        log.info("Электронное письмо успешно отправлено на адрес: {}", email);
        return new SimpleResponse(userAccount.getTokenPassword(), HttpStatus.OK);
    }
    @Override
    public AuthenticationResponse passwordRecovery(String token, String newPassword) {
        UserAccount userAccount = userAccountRepo.getByUserAccountByTokenPassword(token).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пациент с таким email: %s не существует!", token)));
        log.info("аккаунт найден : " + userAccount);
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
                String jwt = jwtService.generateToken(userAccount.getEmail());
                return AuthenticationResponse.builder()
                        .email(userAccount.getEmail())
                        .role(userAccount.getRole())
                        .token(jwt)
                        .build();
            }
        } else {
            throw new RuntimeException("Invalid verification code.");
        }
    }

    @Override
    public SimpleResponse sendMassage(String email, String code, String subject) throws MessagingException {
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пользователь с таким email: %s не существует!", email)));
        User user = userAccount.getUser();
        Context context = new Context();
        context.setVariable("userName", user.getFirstName() + " " + user.getLastName());
        context.setVariable("greeting", getGreeting());
        context.setVariable("verificationCode", code);
        String emailContent = templateEngine.process("registrationCode", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailUsername);
        helper.setSubject(subject);
        helper.setTo(email);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
        log.info("Электронное письмо успешно отправлено на адрес: {}", email);
        return new SimpleResponse(String.format("Код для подтверждения онлайн записи отправлен: %s", email), HttpStatus.OK);
    }

    public boolean isVerificationCodeExpired(UserAccount request) {
        Date currentTime = new Date();
        Date sentTime = request.getVerificationCodeTime();
        long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(currentTime.getTime() - sentTime.getTime());
        return minutesDifference > verificationCodeExpirationMinutes;
    }

    public String getGreeting() {
        LocalTime time = LocalTime.now();
        String greeting;
        if (time.isBefore(LocalTime.NOON)) {
            greeting = "Доброе утро";
        } else if (time.isBefore(LocalTime.of(18, 0))) {
            greeting = "Добрый день";
        } else if (time.isBefore(LocalTime.of(21, 0))) {
            greeting = "Добрый вечер";
        } else {
            greeting = "Доброй ночи";
        }
        return greeting;
    }
}
