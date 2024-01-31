package healthcheck.service.Impl;

import healthcheck.dto.Appointment.AppointmentResponse;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    public void buildAppointmentConfirmationEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(() ->
                new NotFoundException("User is not found !!!"));

        User user = userAccount.getUser();

        String greeting = getGreeting();
        String userName = user.getFirstName() + " " + user.getLastName();

        String emailContent = "<!DOCTYPE html>" +
                "<html lang=\"ru\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Подтверждение записи</title>" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600&display=swap\" rel=\"stylesheet\">" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Montserrat', sans-serif;" +
                "            background-color: #f7f7f7;" +
                "            padding: 20px;" +
                "            margin: 0;" +
                "        }" +
                "        .container {" +
                "            background-color: #fff;" +
                "            padding: 30px;" +
                "            border-radius: 10px;" +
                "            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);" +
                "            max-width: 600px;" +
                "            margin: 0 auto;" +
                "        }" +
                "        h1 {" +
                "            text-align: center;" +
                "            color: #007bff;" +
                "            font-size: 36px;" +
                "            font-weight: 600;" +
                "            margin-top: 0;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        p {" +
                "            font-size: 18px;" +
                "            line-height: 1.6;" +
                "            color: #333;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .image-container {" +
                "            text-align: center;" +
                "            margin-top: 20px;" +
                "        }" +
                "        .greetings-image {" +
                "            max-width: 100%;" +
                "            border-radius: 10px;" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            background-color: #007bff;" +
                "            color: #fff;" +
                "            font-size: 18px;" +
                "            font-weight: 600;" +
                "            text-decoration: none;" +
                "            padding: 10px 20px;" +
                "            border-radius: 5px;" +
                "            transition: all 0.3s ease-in-out;" +
                "        }" +
                "        .button:hover {" +
                "            background-color: #0062cc;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "    <h1>Подтверждение записи</h1>" +
                "    <p style=\"color: #007bff; font-weight: bold;\">" + greeting + "</p>" +
                "    <p>Уважаемый(ая) " + userName + ",</p>" +
                "    <p>Ваша запись на " + LocalDate.now() + " успешно подтверждена.</p>" +
                "    <p>Благодарим за выбор нашей клиники!</p>" +
                "    <div class=\"image-container\">" +
                "        <img src=\"cid:greetingsImage\" class=\"greetings-image\" alt=\"Greetings Image\">" +
                "    </div>" +
                "</div>" +
                "</body>" +
                "</html>";

        sendEmail(userAccount.getEmail(), emailContent);
    }

    private void sendEmail(String to, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
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
