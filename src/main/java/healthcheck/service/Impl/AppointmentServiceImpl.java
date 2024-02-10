package healthcheck.service.Impl;

import healthcheck.dto.Appointment.*;
import healthcheck.dto.SimpleResponse;
import healthcheck.email.EmailService;
import healthcheck.entities.*;
import healthcheck.enums.Facility;
import healthcheck.enums.Status;
import healthcheck.exceptions.AlreadyExistsException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.*;
import healthcheck.repo.Dao.AppointmentDao;
import healthcheck.service.AppointmentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final UserAccountRepo userAccountRepo;
    private final JavaMailSender mailSender;
    private final DoctorRepo doctorRepo;
    private final TimeSheetRepo timeSheetRepo;
    private final EmailService emailService;
    private final DepartmentRepo departmentRepo;
    private final AppointmentDao appointmentDao;

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
            String templatePath = "confirmation_email";
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

    @Override
    public OnlineAppointmentResponse addAppointment(Facility facility,AppointmentRequest request) throws MessagingException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info(email);
        UserAccount userAccount = userAccountRepo.findUserAccountByEmail(email);
        log.info("User account found: " + userAccount);
        Department department = departmentRepo.findByFacility(facility);
        log.info("Department found: " + department);
        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found by ID: " + request.getDoctorId()));
        log.info("Doctor found: " + doctor);
        LocalDate dateOfConsultation = LocalDate.parse(request.getDate());
        LocalTime startOfConsultation = LocalTime.parse(request.getStartTimeConsultation());
        log.info("startOfConsultation :"+startOfConsultation);
        LocalTime endOfConsultation = timeSheetRepo.getTimeSheetByEndTimeOfConsultation(doctor.getId(),startOfConsultation);
        log.info("endOfConsultation :" + endOfConsultation);
        log.info("Creating appointment for user: " + userAccount.getUser().getFirstName() + " " +
                userAccount.getUser().getLastName() + " with doctor: " + doctor.getFullNameDoctor() +
                " on date: " + dateOfConsultation + " at time: " + startOfConsultation);
        Boolean booked = timeSheetRepo.booked(doctor.getSchedule().getId(),dateOfConsultation, startOfConsultation);
        if (booked != null && booked) {
            throw new AlreadyExistsException("Это время занято!");
        } else if (booked == null) {
            try {
                throw new BadRequestException("Этот специалист не работает в этот день или в это время! Рабочие даты: с" + doctor.getSchedule().getStartDateWork() + " to " + doctor.getSchedule().getEndDateWork());
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        Appointment appointment = Appointment.builder()
                .user(userAccount.getUser())
                .department(department)
                .doctor(doctor)
                .appointmentDate(dateOfConsultation)
                .appointmentTime(startOfConsultation)
                .status(Status.CONFIRMED)
                .verificationCode(generateVerificationCode())
                .build();
        updateAvailability(doctor.getId(), dateOfConsultation, startOfConsultation,
                endOfConsultation, false);
        appointmentRepo.save(appointment);
        log.info("успешно обновлен бронирование на true ");
        emailService.sendMassage(request.getEmail(),appointment.getVerificationCode(),"Код для онлайн регистрации !");
        log.info("Электронное письмо успешно отправлено на адрес: {}", email);
        return OnlineAppointmentResponse.builder()
                .id(appointment.getId())
                .dayOfWeek(getDayOfWeek(dateOfConsultation).name())
                .dateOfAppointment(dateOfConsultation)
                .startTimeOfConsultation(startOfConsultation)
                .endTimeOfConsultation(endOfConsultation)
                .imageDoctors(doctor.getImage())
                .fullNameDoctors(doctor.getFullNameDoctor())
                .facility(department.getFacility().name())
                .verificationCode(appointment.getVerificationCode())
                .build();
    }

    public void updateAvailability(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime,boolean available) {
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Доктор с id: " + doctorId + " не найден"));
        Schedule schedule = doctor.getSchedule();
        List<TimeSheet> timeSheets = schedule.getTimeSheets();
        for (LocalTime currentTime = startTime; currentTime.isBefore(endTime); currentTime = currentTime.plusMinutes(schedule.getIntervalInMinutes().getValue())) {
            LocalTime finalCurrentTime = currentTime;
            LocalTime finalCurrentTime1 = currentTime;
            TimeSheet timeSheet = timeSheets.stream()
                    .filter(sheet -> sheet.getDateOfConsultation().isEqual(date) && sheet.getStartTimeOfConsultation().equals(finalCurrentTime))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Не удалось найти TimeSheet для указанного времени: " + date + " " + finalCurrentTime1));
            timeSheet.setAvailable(!available);
            timeSheetRepo.save(timeSheet);
        }
    }

    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }

    @Override
    public SimpleResponse verifyAppointment(Long appointmentId, String verificationCode) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(()-> new NotFoundException("not found "));
        String checkVerificationCode =appointment.getVerificationCode();
        if (checkVerificationCode.equals(verificationCode)) {
            appointment.setStatus(Status.FINISHED);
            appointment.setVerificationCode(null);
            appointmentRepo.save(appointment);
            return new SimpleResponse("Пациент успешно записан", HttpStatus.OK);
        } else {
            return new SimpleResponse("Не правильный код регистрации",HttpStatus.CONFLICT);
        }
    }

    @Override
    public SimpleResponse deleteAppointment(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("not found appointment by id: "+id));
        appointmentRepo.delete(appointment);
        return new SimpleResponse("успешно удален ", HttpStatus.OK);
    }

    @Override
    public FindByDoctorForAppointment findByDoctorId(Long id) {
        Doctor doctor = doctorRepo
                .findById(id).orElseThrow(()-> new NotFoundException("not found Doctor by Id: "+id));
        return FindByDoctorForAppointment.builder()
                .image(doctor.getImage())
                .fullNameDoctor(doctor.getFullNameDoctor())
                .facility(doctor.getDepartment().getFacility().name())
                .build();
    }

    @Override
    public List<AppointmentScheduleTimeSheetResponse> getTheDoctorFreeTimeInTheCalendar(String startDate, String endDate, Long doctorId) {
        return appointmentDao.getTheDoctorFreeTimeInTheCalendar(startDate,endDate,doctorId);
    }

    @Override
    public List<AppointmentResponse> getAllAppointmentDefault() {
        List<Appointment> all = appointmentRepo.getAllAppointmentDefault();
        List<AppointmentResponse> response = new ArrayList<>();

        for (Appointment appointment : all) {

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
                    .build());
        }
        return response;
    }

    @Override
    public SimpleResponse deleteAppointmentById(Long id) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(() ->
                new NotFoundException("Appointment не найден"));
        if (appointment.isProcessed()) {
            appointmentRepo.delete(appointment);
            return SimpleResponse.builder().message("Успешно удален").httpStatus(HttpStatus.OK).build();
        }else {
            log.error("Appointment с ID: " + id + " не обработан");
            return new SimpleResponse("Ошибка Appointment с ID: "+id +" не обработан", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean updateProcessed(AppointmentProcessedRequest request) {
            try {
                Optional<Appointment> appointmentOptional = appointmentRepo.findById(request.getId());
                Appointment application = appointmentOptional.orElseThrow(() -> new NotFoundException("Не найдена заявка с ID: " + request.getId()));
                log.info("Заявка найдена по ID: " + request.getId());
                application.setProcessed(true);
                log.info("Заявка успешно обновлена, статус обработки: " + application.isProcessed());
                appointmentRepo.save(application);
                return application.isProcessed();
            } catch (NotFoundException e) {
                log.error("Ошибка обработки заявки: " + e.getMessage());
                throw e; // выбросить исключение
            }
    }

    @Override
    public SimpleResponse deleteAllAppointmentsById(List<Long> listId) {
        try {
            log.info("Список ID в методе удаления всех: {}", listId);
            List<Appointment> appointments = appointmentRepo.findAllById(listId);
            log.info("Найдены заявки");
            appointmentRepo.deleteAll(appointments);
            log.info("Заявки успешно удалены");
            return new SimpleResponse("Заявки успешно удалены", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            log.error("Ошибка удаления заявок: Некоторые заявки не найдены");
            return new SimpleResponse("Ошибка удаления заявок: Некоторые заявки не найдены", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Ошибка удаления заявок: " + e.getMessage());
            return new SimpleResponse("Ошибка удаления заявок: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


// deleteAllAppointmentById delete
//    @Override
//    public SimpleResponse deleteAllAppointmentById(List<AppointmentProcessedRequest> appointmentDeleteRequests) {
//        List<Long> failedToDelete = appointmentDeleteRequests.stream()
//                .filter(deleteRequest -> !deleteRequest.isActive())
//                .map(AppointmentDeleteRequest::getId)
//                .toList();
//
//        if (!failedToDelete.isEmpty()) {
//            return SimpleResponse.builder()
//                    .message("Нельзя удалить записи с ID: " + failedToDelete + ", так как их статус 'active' не совпадает с указанным в запросе")
//                    .httpStatus(HttpStatus.BAD_REQUEST)
//                    .build();
//        }
//
//        appointmentDeleteRequests.stream()
//                .filter(AppointmentDeleteRequest::isActive)
//                .map(AppointmentDeleteRequest::getId)
//                .forEach(appointmentId -> {
//                    Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(() ->
//                            new NotFoundException("Appointment не найден"));
//                    appointmentRepo.delete(appointment);
//                });
//
//        return SimpleResponse.builder()
//                .message("Записи успешно удалены")
//                .httpStatus(HttpStatus.OK)
//                .build();
//    }
}
