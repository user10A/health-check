package healthcheck.service.Impl;

import healthcheck.dto.Appointment.*;
import healthcheck.dto.SimpleResponse;
import healthcheck.email.EmailService;
import healthcheck.entities.*;
import healthcheck.enums.Facility;
import healthcheck.enums.Status;
import healthcheck.exceptions.AlreadyExistsException;
import healthcheck.exceptions.BadCredentialsException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.UserAccountRepo;
import healthcheck.repo.DoctorRepo;
import healthcheck.repo.AppointmentRepo;
import healthcheck.repo.TimeSheetRepo;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.Dao.AppointmentDao;
import healthcheck.service.AppointmentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Random;

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
    private final MessageSource messageSource;
    @Override
    public List<AppointmentResponse> getAllAppointment(String word) {
        log.info("Запрос на получение всех приемов для слова: {}", word);
        return appointmentDao.getAllAppointment(word);
    }
    @Override
    public SimpleResponse appointmentConfirmationEmail(Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount userAccount = userAccountRepo.getUserAccountByEmail(email).orElseThrow(() ->
                new NotFoundException("error.email_not_found",new Object[]{email}));
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(()-> new NotFoundException("error.appointment_not_found",new Object[]{appointmentId}));
        User user = userAccount.getUser();
        String userName = user.getFirstName() + " " + user.getLastName();
        Map<String, String> variables = new HashMap<>();
        variables.put("greeting", emailService.getGreeting());
        variables.put("userName", userName);
        int day = appointment.getAppointmentDate().getDayOfMonth();
        String month = appointment.getAppointmentDate().getMonth().getDisplayName(TextStyle.FULL, LocaleContextHolder.getLocale());
        String time = appointment.getAppointmentTime().toString();
        String dayOfMonth =(day+" "+month+" в "+time);
        variables.put("dayOfMonth",dayOfMonth);
        sendEmail(userAccount.getEmail(), variables);
        return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("message.response",
                null, LocaleContextHolder.getLocale()));}
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
            helper.setSubject(messageSource.getMessage("message.confirmation_response",null,LocaleContextHolder.getLocale()));
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            log.info("Ошибка из : MessagingException ");
        }
    }

    @Override
    @Transactional
    public SimpleResponse addAppointment(Facility facility, AppointmentRequest request) throws MessagingException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info(email);

        UserAccount userAccount = userAccountRepo.findUserAccountByEmail(request.getEmail());
        if (userAccount==null){
            throw new NotFoundException("error.email_not_found", new Object[]{request.getEmail()});
        }
        log.info("User account found: " + userAccount);

        Department department = departmentRepo.findByFacility(facility);
        log.info("Department found: " + department);

        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("error.doctor_not_found",
                        new Object[]{request.getDoctorId()}));

        if (!department.getDoctors().contains(doctor))
            throw new NotFoundException("error.doctor_not_found_department",
                    new Object[]{facility});
        log.info("Doctor found: %s"+ doctor);

        LocalDate dateOfConsultation = LocalDate.parse(request.getDate());
        LocalTime startOfConsultation = LocalTime.parse(request.getStartTimeConsultation());
        log.info("startOfConsultation :"+startOfConsultation);
        log.info("Creating appointment for user: " + userAccount.getUser().getFirstName() + " " +
                userAccount.getUser().getLastName() + " with doctor: " + doctor.getFullNameDoctor() +
                " on date: " + dateOfConsultation + " at time: " + startOfConsultation);
        Boolean booked = timeSheetRepo.booked(doctor.getSchedule().getId(),dateOfConsultation, startOfConsultation);
        String date= doctor.getSchedule().getStartDateWork()+"-"+ doctor.getSchedule().getEndDateWork();
        if (booked != null && booked) {
            throw new AlreadyExistsException("error.timeSheet_alreadyExists");
        } else if (booked == null) {
            try {
                throw new BadCredentialsException("error.doctor_not_found_department",
                        new Object[]{date});
            } catch (BadCredentialsException e) {
                throw new RuntimeException(e);
            }
        }
        TimeSheet timeSheet = timeSheetRepo.getTimeSheetByDoctorIdAndStartTime(doctor.getId(), dateOfConsultation, startOfConsultation);
        Appointment appointment = Appointment.builder()
                .user(userAccount.getUser())
                .department(department)
                .doctor(doctor)
                .appointmentDate(dateOfConsultation)
                .appointmentTime(startOfConsultation)
                .status(Status.CONFIRMED)
                .verificationCode(generateVerificationCode())
                .build();
        appointmentRepo.save(appointment);
        timeSheet.setAvailable(true);
        timeSheetRepo.save(timeSheet);
        log.info("успешно обновлен бронирование на true ");
        log.info("Электронное письмо успешно отправлено на адрес: {}", email);
        emailService.sendMassage(email,appointment.getVerificationCode(),
                messageSource.getMessage("message.code_response",null,LocaleContextHolder.getLocale()));
        return new SimpleResponse(appointment.getId()+" "+appointment.getVerificationCode(), HttpStatus.OK);
    }
    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }

    @Override
    public AppointmentResponseById verifyAppointment(Long appointmentId, String verificationCode) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(()-> new NotFoundException("error.appointment_not_found",
                new Object[]{appointmentId}));

        String checkVerificationCode =appointment.getVerificationCode();

        if (checkVerificationCode.equals(verificationCode)) {
            appointment.setStatus(Status.FINISHED);
            appointment.setVerificationCode(null);
            appointmentRepo.save(appointment);
            TimeSheet timeSheet = timeSheetRepo.getTimeSheetByDoctorIdAndStartTime(appointment.getDoctor().getId(),appointment.getAppointmentDate(),appointment.getAppointmentTime());
            return  AppointmentResponseById.builder()
                    .id(appointment.getId())
                    .doctorImage(appointment.getDoctor().getImage())
                    .localDate(appointment.getAppointmentDate())
                    .startTime(timeSheet.getStartTimeOfConsultation())
                    .endTime(timeSheet.getEndTimeOfConsultation())
                    .dayOfWeek(getDayOfWeek(appointment.getAppointmentDate()).name())
                    .doctorFullName(appointment.getDoctor().getFullNameDoctor())
                    .facility(appointment.getDepartment().getFacility().name())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public SimpleResponse deleteAppointment(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("error.appointment_not_found",
                        new Object[]{id}));

        appointment.setStatus(Status.CANCELLED);
        appointmentRepo.save(appointment);
        TimeSheet timeSheet = timeSheetRepo.getTimeSheetByDoctorIdAndStartTime(appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime());
        timeSheet.setAvailable(false);
        timeSheetRepo.save(timeSheet);

        return new SimpleResponse(HttpStatus.OK,messageSource.getMessage("message.canceled",null,LocaleContextHolder.getLocale()));
    }

    @Override
    public FindDoctorForAppointmentResponse findByDoctorId(Long id) {
        Doctor doctor = doctorRepo
                .findById(id).orElseThrow(()-> new NotFoundException("error.doctor_not_found",
                        new Object[]{id}));

        return FindDoctorForAppointmentResponse.builder()
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
       return appointmentDao.getAllAppointmentDefault();
    }

    @Override
    public SimpleResponse deleteAppointmentById(Long id) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(() ->
                new NotFoundException("error.appointment_not_found",
                        new Object[]{id}));
        if (appointment.isProcessed()) {
            appointmentRepo.delete(appointment);
            return new SimpleResponse(HttpStatus.OK,messageSource.getMessage("message.delete_response",null,LocaleContextHolder.getLocale()));
        }else {
            log.error("Appointment с ID: " + id + " не обработан");
            return new SimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR,messageSource.getMessage("error.appointment_response_bad_request",null,LocaleContextHolder.getLocale()));
        }
    }

    @Override
    @Transactional
    public boolean updateProcessed(AppointmentProcessedRequest request) {
            try {
                Appointment appointment = appointmentRepo.findById(request.getId()).orElseThrow(()-> new NotFoundException("error.appointment_not_found",
                        new Object[]{request.getId()}));
                log.info("Заявка найдена по ID: " + request.getId());
                appointment.setProcessed(request.isActive());
                log.info("Заявка успешно обновлена, статус обработки: " + appointment.isProcessed());
                appointmentRepo.save(appointment);
                return appointment.isProcessed();
            } catch (Exception e) {
                log.error("Ошибка обработки заявки: " + e.getMessage());
                throw e;
            }
    }

    @Override
    @Transactional
    public SimpleResponse deleteAllAppointmentsById(List<Long> listId) {
        try {
            log.info("Список ID в методе удаления всех: {}", listId);

            List<Appointment> appointments = appointmentRepo.findAllById(listId);
            log.info("Найдены заявки");
            appointmentRepo.deleteAll(appointments);
            log.info("Заявки успешно удалены");

            return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("message.delete_response",null,LocaleContextHolder.getLocale()));
        } catch (EmptyResultDataAccessException e) {
            log.error("Ошибка удаления заявок: Некоторые заявки не найдены");
            return new SimpleResponse(HttpStatus.NOT_FOUND,messageSource.getMessage("error.appointment_response_bad_request_all",null,LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("Ошибка удаления заявок: " + e.getMessage());
            return new SimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR, messageSource.getMessage("error.appointment_response_internalServerError",null,LocaleContextHolder.getLocale()));
        }
    }
}