package healthcheck.service.Impl;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.Schedule.ScheduleUpdateRequest;
import healthcheck.dto.Schedule.TimeSheetDeleteRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import healthcheck.entities.Schedule;
import healthcheck.entities.TimeSheet;
import healthcheck.dto.Schedule.PatternTimeSheetRequest;
import healthcheck.enums.DaysOfRepetition;
import healthcheck.enums.Facility;
import healthcheck.exceptions.AlreadyExistsException;
import healthcheck.exceptions.BadCredentialsException;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.ScheduleDao;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.DoctorRepo;
import healthcheck.repo.ScheduleRepo;
import healthcheck.repo.TimeSheetRepo;
import healthcheck.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final DoctorRepo doctorRepo;
    private final DepartmentRepo departmentRepo;
    private final ScheduleRepo scheduleRepo;
    private final TimeSheetRepo timeSheetRepo;
    private final ScheduleDao scheduleDao;
    private final MessageSource messageSource;

    @Override
    public SimpleResponse saveSchedule(@NonNull Facility facility, @NonNull Long doctorId,
                                       @Valid @NonNull AddScheduleRequest addScheduleRequest) {
        Department department = departmentRepo.getDepartmentByFacility(facility)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.department_not_found",
                        new Object[]{facility}, LocaleContextHolder.getLocale())));

        Doctor doctor = doctorRepo.findById(doctorId)
                .filter(d -> department.getDoctors().contains(d))
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.doctor_not_found_department",
                        new Object[]{doctorId}, LocaleContextHolder.getLocale())));

        if (doctor.getSchedule() != null) {
            throw new AlreadyExistsException(messageSource.getMessage("error.schedule_exists",
                    null, LocaleContextHolder.getLocale()));
        }

        validateDateRange(addScheduleRequest.getCreateStartDate(), addScheduleRequest.getCreateEndDate());
        validateTimeRange(addScheduleRequest.getStartTime(), addScheduleRequest.getEndTime());

        Map<DaysOfRepetition, Boolean> days = addScheduleRequest.getDayOfWeek().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> DaysOfRepetition.fromName(String.valueOf(entry.getKey())),
                        Map.Entry::getValue
                ));

        Schedule schedule = Schedule.builder()
                .startDateWork(addScheduleRequest.getCreateStartDate())
                .endDateWork(addScheduleRequest.getCreateEndDate())
                .startDayTime(LocalTime.parse(addScheduleRequest.getStartTime()))
                .endDayTime(LocalTime.parse(addScheduleRequest.getEndTime()))
                .startBreakTime(LocalTime.parse(addScheduleRequest.getStartBreak()))
                .endBreakTime(LocalTime.parse(addScheduleRequest.getEndBreak()))
                .intervalInMinutes(addScheduleRequest.getInterval())
                .dayOfWeek(days)
                .doctor(doctor)
                .department(department)
                .build();

        scheduleRepo.save(schedule);

        LocalDate currentDate = addScheduleRequest.getCreateStartDate();
        LocalDate endDate = addScheduleRequest.getCreateEndDate();
        LocalTime startTime = LocalTime.parse(addScheduleRequest.getStartTime());
        LocalTime endTime = LocalTime.parse(addScheduleRequest.getEndTime());
        LocalTime startBreakTime = LocalTime.parse(addScheduleRequest.getStartBreak());
        LocalTime endBreakTime = LocalTime.parse(addScheduleRequest.getEndBreak());
        int intervalInMinutes = addScheduleRequest.getInterval().getValue();

        while (!currentDate.isAfter(endDate)) {
            for (DaysOfRepetition day : DaysOfRepetition.values()) {
                if (days.getOrDefault(day, false) && currentDate.getDayOfWeek().name().equals(day.name())) {
                    generateTimeSlotsForDay(currentDate, startTime, endTime, startBreakTime, endBreakTime, intervalInMinutes, schedule);
                }
            }
            currentDate = currentDate.plusDays(1);
        }

        generateTimeSheets(currentDate, addScheduleRequest.getCreateEndDate(), days, startTime, endTime, startBreakTime, endBreakTime, intervalInMinutes, schedule);

        return new SimpleResponse(HttpStatus.OK, messageSource.
                getMessage("error.schedule_save", null, LocaleContextHolder.getLocale()));
    }

    @Override
    public SimpleResponse updateScheduleByDoctorId(Long doctorId, LocalDate date, List<ScheduleUpdateRequest> timeSlots) {
        try {
            Doctor doctor = doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.doctor_not_found",
                            new Object[]{doctorId}, LocaleContextHolder.getLocale())));

            Schedule schedule = Optional.ofNullable(doctor.getSchedule()).orElseGet(Schedule::new);

            List<TimeSheet> timeSheets = new ArrayList<>(Optional.ofNullable(schedule.getTimeSheets()).orElse(new ArrayList<>()).stream()
                    .filter(timeSheet -> timeSheet.getDateOfConsultation().equals(date))
                    .toList());

            LocalTime endTimeOfWork = schedule.getEndDayTime();

            for (ScheduleUpdateRequest timeSlot : timeSlots) {
                LocalTime startTimeOfConsultation = LocalTime.parse(timeSlot.getFromTime());
                LocalTime endTimeOfConsultation = LocalTime.parse(timeSlot.getToTime());

                if (endTimeOfConsultation.isAfter(endTimeOfWork)) {
                    schedule.setEndDayTime(endTimeOfConsultation);
                }

                if (Duration.between(startTimeOfConsultation, endTimeOfConsultation).toMinutes() < 10) {
                    throw new RuntimeException(messageSource.getMessage("error.timeSheet",null,LocaleContextHolder.getLocale()));
                }

                boolean timeSlotExists = timeSheets.stream()
                        .anyMatch(timeSheet ->
                                timeSheet.getDateOfConsultation().equals(date) &&
                                        !timeSheet.getEndTimeOfConsultation().isBefore(startTimeOfConsultation) &&
                                        !timeSheet.getStartTimeOfConsultation().isAfter(endTimeOfConsultation) &&
                                        !timeSheet.getEndTimeOfConsultation().equals(startTimeOfConsultation)
                        );

                if (timeSlotExists) {
                    throw new BadCredentialsException(messageSource.getMessage("error.timeSheet_alreadyExists",
                            null, LocaleContextHolder.getLocale()));
                }

                TimeSheet newTimeSheet = TimeSheet.builder()
                        .startTimeOfConsultation(startTimeOfConsultation)
                        .endTimeOfConsultation(endTimeOfConsultation)
                        .dateOfConsultation(date)
                        .schedule(schedule)
                .build();

                scheduleRepo.save(schedule);
                timeSheets.add(newTimeSheet);
                timeSheetRepo.save(newTimeSheet);
            }

            schedule.setTimeSheets(timeSheets);
            doctor.setSchedule(schedule);

            doctorRepo.save(doctor);
            log.info("Расписание доктора успешно обновлено: {}", doctorId);

            return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("error.schedule_update",
                    null, LocaleContextHolder.getLocale()));
        } catch (NotFoundException e) {
            log.error("Ошибка в методе updateScheduleByDoctorId: {}", e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Ошибка обновления расписания доктора: {}", ex.getMessage(), ex);
            throw new DataUpdateException(messageSource.getMessage("error.schedule_data_update_exception",
                    null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public List<ResponseToGetSchedules> getAllSchedules() {
        return scheduleDao.getAllSchedules();
    }

    @Override
    public List<ResponseToGetSchedules> getScheduleByDate(LocalDate startDate, LocalDate endDate) {
        return scheduleDao.getScheduleByDate(startDate, endDate);
    }

    @Override
    public List<ResponseToGetSchedules> getScheduleBySearch(String word) {
        return scheduleDao.getScheduleBySearch(word);
    }

    private void generateTimeSheets(LocalDate currentDate, LocalDate endDate, Map<DaysOfRepetition, Boolean> days,
                                    LocalTime startTime, LocalTime endTime, LocalTime startBreakTime,
                                    LocalTime endBreakTime, int intervalInMinutes, Schedule schedule) {
        if (currentDate.isAfter(endDate)) {
            return;
        }

        for (DaysOfRepetition day : DaysOfRepetition.values()) {
            if (days.getOrDefault(day, false)) {
                generateTimeSlotsForDay(currentDate, startTime, endTime, startBreakTime, endBreakTime, intervalInMinutes, schedule);
            }
            currentDate = currentDate.plusDays(1);
        }
        generateTimeSheets(currentDate, endDate, days, startTime, endTime, startBreakTime, endBreakTime, intervalInMinutes, schedule);
    }

    private void generateTimeSlotsForDay(LocalDate currentDate, LocalTime startTime, LocalTime endTime,
                                         LocalTime staticStartBreakTime, LocalTime staticEndBreakTime,
                                         int intervalInMinutes, Schedule schedule) {
        if (startTime.isBefore(endTime)) {
            LocalTime endTimeOfConsultation = startTime.plusMinutes(intervalInMinutes);

            if (!endTimeOfConsultation.isAfter(staticStartBreakTime)) {
                TimeSheet timeSheet = TimeSheet.builder()
                        .dateOfConsultation(currentDate)
                        .startTimeOfConsultation(startTime)
                        .endTimeOfConsultation(endTimeOfConsultation)
                        .schedule(schedule)
                        .available(false)
                        .build();
                timeSheetRepo.save(timeSheet);

                startTime = endTimeOfConsultation;
            } else if (endTimeOfConsultation.isAfter(staticEndBreakTime) && endTimeOfConsultation.isAfter(staticStartBreakTime)) {
                TimeSheet timeSheet = TimeSheet.builder()
                        .dateOfConsultation(currentDate)
                        .startTimeOfConsultation(startTime)
                        .endTimeOfConsultation(endTimeOfConsultation)
                        .schedule(schedule)
                        .available(false)
                        .build();
                timeSheetRepo.save(timeSheet);

                startTime = endTimeOfConsultation;
            } else {
                startTime = staticEndBreakTime;
            }

            generateTimeSlotsForDay(currentDate, startTime, endTime, staticStartBreakTime, staticEndBreakTime, intervalInMinutes, schedule);
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(messageSource.getMessage("error.schedule_illegal_argument_exception_validateDateRange",
                    null, LocaleContextHolder.getLocale()));
        }
    }

    private void validateTimeRange(String startTime, String endTime) {
        if (LocalTime.parse(startTime).isAfter(LocalTime.parse(endTime))) {
            throw new IllegalArgumentException(messageSource.getMessage("error.schedule_illegal_argument_exception_validateTimeRange",
                    null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public SimpleResponse savePatternTimeSheet(PatternTimeSheetRequest request) {
        Doctor doctor = doctorRepo.findById(request.getDoctorId()).orElseThrow(() ->
                new NotFoundException(messageSource.getMessage("error.doctor_not_found",
                        new Object[]{request.getDoctorId()}, LocaleContextHolder.getLocale())));

        Pageable pageable = PageRequest.of(0, 1);
        Page<TimeSheet> result = timeSheetRepo.getTimeSheetByDoctorIdAndStartTime1(doctor.getId()
                ,request.getDateOfConsultation()
                ,pageable);
        TimeSheet timeSheet = result.getContent().isEmpty() ? null : result.getContent().get(0);

        log.info("TimeSheet: {}", timeSheet);

        if (timeSheet != null) {
            log.error("Уже есть данные");
            throw new AlreadyExistsException(messageSource.getMessage("error.schedule_exists",
                    null, LocaleContextHolder.getLocale()));
        }

        Schedule schedule = doctor.getSchedule();
        LocalDate dateOfConsultation = request.getDateOfConsultation();

        if (dateOfConsultation.isBefore(schedule.getStartDateWork()) || dateOfConsultation.isAfter(schedule.getEndDateWork())) {
            log.error("В этот день доктор не работает");
            throw new BadCredentialsException(messageSource.getMessage("error.schedule_bad_request_exception",
                    null, LocaleContextHolder.getLocale()));
        }

        String days = request.getDateOfConsultation().getDayOfWeek().toString();
        DaysOfRepetition daysOfRepetition = DaysOfRepetition.valueOf(days);

        Map<DaysOfRepetition, Boolean> daysMap = new HashMap<>();
        daysMap.put(daysOfRepetition, true);
        schedule.setDayOfWeek(daysMap);
        scheduleRepo.save(schedule);

        generateTimeSlotsForDay(dateOfConsultation, schedule.getStartDayTime(), schedule.getEndDayTime(),
                schedule.getStartBreakTime(), schedule.getEndBreakTime(), schedule.getIntervalInMinutes().getValue(),
                schedule);

        log.info("Паттерн расписания успешно сохранен");

        return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("message.save_response",
                null, LocaleContextHolder.getLocale()));
    }

    @Override
    public SimpleResponse deleteTimeSheetByDoctorIdAndDate(Long doctorId, LocalDate date, List<TimeSheetDeleteRequest> request) {
        try {
            Doctor doctor = doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.doctor_not_found",
                            new Object[]{doctorId}, LocaleContextHolder.getLocale())));

            Schedule schedule = doctor.getSchedule();
            List<TimeSheet> timeSheets = schedule.getTimeSheets();

            List<TimeSheet> timeSheetsToDelete = timeSheets.stream()
                    .filter(timeSheet ->
                            timeSheet.getDateOfConsultation().equals(date) &&
                                    containsTimeSheetDeleteRequest(request, timeSheet)
                    )
                    .collect(Collectors.toList());

            if (timeSheetsToDelete.isEmpty()) {
                return new SimpleResponse(HttpStatus.NOT_FOUND, messageSource.getMessage("error.schedule_delete_response",
                        null, LocaleContextHolder.getLocale()));
            }

            timeSheets.removeAll(timeSheetsToDelete);
            timeSheetRepo.deleteAll(timeSheetsToDelete);

            doctorRepo.save(doctor);
            log.info("Записи о приёме успешно удалены для доктора: {}", doctorId);

            return new SimpleResponse(HttpStatus.OK, messageSource.getMessage("message.delete_response",
                    null, LocaleContextHolder.getLocale()));
        } catch (NotFoundException e) {
            log.error("Ошибка в методе deleteTimeSheetByDoctorIdAndDate: {}", e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Ошибка удаления записей о приёме: {}", ex.getMessage(), ex);
            throw new DataUpdateException(messageSource.getMessage("error.schedule_data_update_exception_delete",
                    null, LocaleContextHolder.getLocale()));
        }
    }

    private boolean containsTimeSheetDeleteRequest(List<TimeSheetDeleteRequest> request, TimeSheet timeSheet) {
        return request.stream()
                .anyMatch(deleteRequest ->
                        deleteRequest.getFromTime().equals(timeSheet.getStartTimeOfConsultation().toString())
                );
    }
}