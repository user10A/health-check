package healthcheck.service.Impl;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Appointment;
import healthcheck.entities.User;
import healthcheck.entities.UserAccount;
import healthcheck.enums.Status;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.AppointmentRepo;
import healthcheck.repo.UserAccountRepo;
import healthcheck.service.AppointmentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final UserAccountRepo userAccountRepo;
    private final JavaMailSender mailSender;

    @Override
    public List<AppointmentResponse> getAllAppointment(String word) {
        log.info("Запрос на получение всех приемов для слова: {}", word);

        List<Appointment> all = appointmentRepo.getAllAppointment(word);
        List<AppointmentResponse> response = new ArrayList<>();
        boolean status = false;
        for (Appointment appointment : all) {
            if (appointment.getStatus().equals(Status.CONFIRMED)) {
                status = false;
            } else if (appointment.getStatus().equals(Status.FINISHED)) {
                status = true;
            }

            String username = appointment.getUser().getFirstName() + " " +
                    appointment.getUser().getLastName();

            response.add(AppointmentResponse.builder()
                    .appointmentId(appointment.getId())
                    .fullName(username)
                    .phoneNumber(appointment.getUser().getPhoneNumber())
                    .email(appointment.getUser().getUserAccount().getEmail())
                    .facility(String.valueOf(appointment.getDepartment().getFacility()))
                    .specialist(appointment.getDoctor().getFullNameDoctor())
                    .localDate(appointment.getAppointmentDate())
                    .localTime(appointment.getAppointmentTime())
                    .status(status)
                    .build());
        }
        log.info("Возвращено {} записей о приемах", response.size());
        return response;
    }

    @Override
    public SimpleResponse appointmentConfirmationEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(() ->
                new NotFoundException("User is not found !!!"));

        User user = userAccount.getUser();

        String greeting = getGreeting();
        String userName = user.getFirstName() + " " + user.getLastName();

        Map<String, String> variables = new HashMap<>();
        variables.put("greeting", greeting);
        variables.put("userName", userName);
        variables.put("localDate", LocalDate.now().toString());

        sendEmail(userAccount.getEmail(), variables);
        return SimpleResponse.builder().message("Сообщение успешно отправлено!").httpStatus(HttpStatus.OK).build();
    }

    private void sendEmail(String to, Map<String, String> variables) {
        try {
            String templatePath = "confirmiton_email";
            Resource resource = new ClassPathResource("templates/" + templatePath + ".html");
            String content = Files.readString(resource.getFile().toPath());


            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace("{" + entry.getKey() + "}", entry.getValue());
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject("Подтверждение записи");
            helper.setText(content, true);

            String filePath = "C:/Users/user/Pictures/Без названия.png";
            byte[] imageBytes;
            try (FileInputStream inputStream = new FileInputStream(filePath)) {
                imageBytes = inputStream.readAllBytes();
            }

            ByteArrayResource imageResource = new ByteArrayResource(imageBytes);

            helper.addInline("greetingsImage", imageResource, "image/png");

            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String getGreeting() {
        LocalTime currentTime = LocalTime.now();
        String greeting;

        if (currentTime.isBefore(LocalTime.NOON)) {
            greeting = "Доброе утро";
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            greeting = "Добрый день";
        } else if (currentTime.isBefore(LocalTime.of(21, 0))) {
            greeting = "Добрый вечер";
        } else {
            greeting = "Доброй ночи";
        }

        return greeting;
    }
}