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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private final MessageSource messageSource;

    @Override
    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(
                () -> new NotFoundException(
                        messageSource.getMessage("error.email_not_found",new Object[]{email},LocaleContextHolder.getLocale())));
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
        helper.setSubject(messageSource.getMessage("subject",null,LocaleContextHolder.getLocale()));
        helper.setTo(email);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
        log.info("Электронное письмо успешно отправлено на адрес: {}", email);
        return new SimpleResponse(userAccount.getTokenPassword(), HttpStatus.OK);
    }
    @Override
    public AuthenticationResponse passwordRecovery(String token, String newPassword) throws Exception {
        UserAccount userAccount = userAccountRepo.getByUserAccountByTokenPassword(token).orElseThrow(
                () -> new NotFoundException(
                        messageSource.getMessage("error.email_not_found_token",new Object[]{token},LocaleContextHolder.getLocale())));
        log.info("аккаунт найден : " + userAccount);
        if (token.equals(userAccount.getTokenPassword())) {
            if (isVerificationCodeExpired(userAccount)) {
                userAccount.setTokenPassword(null);
                log.info("вермя истекло");
                throw new Exception(messageSource.getMessage("error.email_verification_code_expired",null,LocaleContextHolder.getLocale()));
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
            throw new Exception(messageSource.getMessage("error.email_verification_code_invalid",null,LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public SimpleResponse sendMassage(String email, String code, String subject) throws MessagingException {
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(
                () -> new NotFoundException(
                        messageSource.getMessage("error.email_not_found",new Object[]{email},LocaleContextHolder.getLocale())));
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
        return new SimpleResponse(messageSource.getMessage("email_verification_code",new Object[]{email},LocaleContextHolder.getLocale()), HttpStatus.OK);
    }

    public boolean isVerificationCodeExpired(UserAccount request) {
        Date currentTime = new Date();
        Date sentTime = request.getVerificationCodeTime();
        long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(currentTime.getTime() - sentTime.getTime());
        return minutesDifference > verificationCodeExpirationMinutes;
    }

    public String getGreeting() {
        ZoneId zoneId = ZoneId.of("Asia/Bishkek");
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        LocalTime time = currentTime.toLocalTime();
        String greeting;
        if (time.isBefore(LocalTime.NOON)) {
            greeting = messageSource.getMessage("greeting_good_morning",null ,LocaleContextHolder.getLocale());
        } else if (time.isBefore(LocalTime.of(18, 0))) {
            greeting = messageSource.getMessage("greeting_good_afternoon",null ,LocaleContextHolder.getLocale());
        } else if (time.isBefore(LocalTime.of(21, 0))) {
            greeting = messageSource.getMessage("greeting_good_evening",null ,LocaleContextHolder.getLocale());
        } else {
            greeting = messageSource.getMessage("greeting_good_night",null ,LocaleContextHolder.getLocale());
        }
        return greeting;
    }
}
