package healthcheck.service.Impl;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ScheduleGetResponse;
import healthcheck.dto.Schedule.ScheduleUpdateRequest;
import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import healthcheck.entities.Schedule;
import healthcheck.entities.TimeSheet;
import healthcheck.enums.DaysOfRepetition;
import healthcheck.enums.Facility;
import healthcheck.excel.ExcelExportUtils;
import healthcheck.exceptions.AlreadyExistsException;
import healthcheck.exceptions.DataUpdateException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.Dao.ScheduleDao;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.DoctorRepo;
import healthcheck.repo.ScheduleRepo;
import healthcheck.repo.TimeSheetRepo;
import healthcheck.service.ScheduleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Override
    public SimpleResponse saveSchedule(@NonNull Facility facility, @NonNull Long doctorId,
                                          @Valid @NonNull AddScheduleRequest addScheduleRequest
    ) {
        Department department = departmentRepo.getDepartmentByFacility(facility)
                .orElseThrow(() -> new NotFoundException("Департамент не найден"));

        Doctor doctor = doctorRepo.findById(doctorId)
                .filter(d -> department.getDoctors().contains(d))
                .orElseThrow(() -> new NotFoundException("Доктор не в данном департаменте или не найден"));

        if (doctor.getSchedule() != null) {
            throw new AlreadyExistsException("Врач с ID: " + doctorId + " уже имеет расписание");
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

        while (!currentDate.isAfter(addScheduleRequest.getCreateEndDate())) {
            for (DaysOfRepetition day : DaysOfRepetition.values()) {
                if (!currentDate.isAfter(addScheduleRequest.getCreateEndDate())) {
                    if (days.getOrDefault(day, false)) {
                        LocalTime startTime = LocalTime.parse(addScheduleRequest.getStartTime());
                        LocalTime endTime = LocalTime.parse(addScheduleRequest.getEndTime());
                        LocalTime currentStartTime = startTime;

                        LocalTime startBreakTime = LocalTime.parse(addScheduleRequest.getStartBreak());
                        LocalTime endBreakTime = LocalTime.parse(addScheduleRequest.getEndBreak());

                        while (currentStartTime.plusMinutes(addScheduleRequest.getInterval().getValue()).isBefore(endTime)) {
                            if (isConsultationTime(currentStartTime, startBreakTime, endBreakTime)) {
                                TimeSheet timeSheet = TimeSheet.builder()
                                        .dateOfConsultation(currentDate)
                                        .startTimeOfConsultation(currentStartTime)
                                        .endTimeOfConsultation(currentStartTime.plusMinutes(addScheduleRequest.getInterval().getValue()))
                                        .schedule(schedule)
                                        .available(false)
                                        .build();
                                timeSheetRepo.save(timeSheet);
                            }
                            currentStartTime = currentStartTime.plusMinutes(addScheduleRequest.getInterval().getValue());
                        }
                    }
                    currentDate = currentDate.plusDays(1);
                }
            }
        }
        return SimpleResponse.builder().message("Успешно создано расписание!").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public ScheduleGetResponse updateScheduleByDoctorId(Long doctorId, LocalDate date,
                                                        List<ScheduleUpdateRequest.TimeSlot> timeSlots) {
        try {
            Doctor doctor = doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new NotFoundException("Доктор не найден"));

            Schedule schedule = Optional.ofNullable(doctor.getSchedule()).orElseGet(Schedule::new);

            List<TimeSheet> timeSheets = new ArrayList<>(Optional.ofNullable(schedule.getTimeSheets()).orElse(new ArrayList<>()).stream()
                    .filter(timeSheet -> timeSheet.getDateOfConsultation().equals(date))
                    .toList());

            if (timeSheets.size() + timeSlots.size() > 8) {
                throw new RuntimeException("Достигнуто максимальное количество окошек для доктора");
            }

            LocalTime endTimeOfWork = schedule.getEndDayTime();

            for (ScheduleUpdateRequest.TimeSlot timeSlot : timeSlots) {
                LocalTime startTimeOfConsultation = LocalTime.parse(timeSlot.getFromTime());
                LocalTime endTimeOfConsultation = LocalTime.parse(timeSlot.getToTime());

                if (endTimeOfConsultation.isAfter(endTimeOfWork)) {
                    schedule.setEndDayTime(endTimeOfConsultation);
                }

                boolean timeSlotExists = timeSheets.stream()
                        .anyMatch(timeSheet ->
                                timeSheet.getDateOfConsultation().equals(date) &&
                                        !timeSheet.getEndTimeOfConsultation().isBefore(startTimeOfConsultation) &&
                                        !timeSheet.getStartTimeOfConsultation().isAfter(endTimeOfConsultation.plusMinutes(10))
                        );

                if (timeSlotExists) {
                    throw new RuntimeException("Время консультации уже занято");
                }

                TimeSheet newTimeSheet = new TimeSheet();
                newTimeSheet.setStartTimeOfConsultation(startTimeOfConsultation);
                newTimeSheet.setEndTimeOfConsultation(endTimeOfConsultation);
                newTimeSheet.setDateOfConsultation(date);
                newTimeSheet.setSchedule(schedule);

                timeSheets.add(newTimeSheet);
                timeSheetRepo.save(newTimeSheet);
            }

            schedule.setTimeSheets(timeSheets);
            doctor.setSchedule(schedule);

            doctorRepo.save(doctor);
            log.info("Расписание доктора успешно обновлено: {}", doctorId);

            return ScheduleGetResponse.builder()
                    .department(doctor.getDepartment())
                    .timeSheets(timeSheets)
                    .doctorFullName(doctor.getFullNameDoctor())
                    .localDateConsultation(date)
                    .build();
        } catch (NotFoundException e) {
            log.error("Ошибка в методе updateScheduleByDoctorId: {}", e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Ошибка обновления расписания доктора: {}", ex.getMessage(), ex);
            throw new DataUpdateException("Ошибка обновления расписания доктора");
        }
    }

      public List<ResponseToGetSchedules> getAllSchedules() {
        return scheduleDao.getAllSchedules();
    }

    @Override
    public List<ResponseToGetSchedules> getScheduleByDate(String startDate, String endDate) {
        return scheduleDao.getScheduleByDate(startDate, endDate);
    }

    @Override
    public List<ResponseToGetSchedules> getScheduleBySearch(String word) {
        return scheduleDao.getScheduleBySearch(word);
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Дата начала не может быть после даты окончания");
        }
    }

    private void validateTimeRange(String startTime, String endTime) {
        if (LocalTime.parse(startTime).isAfter(LocalTime.parse(endTime))) {
            throw new IllegalArgumentException("Время начала не может быть после времени окончания");
        }
    }

    private boolean isConsultationTime(LocalTime currentTime, LocalTime startBreakTime, LocalTime endBreakTime) {
        return currentTime.isBefore(startBreakTime) || currentTime.isAfter(endBreakTime);
    }

    public List<Schedule> exportCustomerToExcel(HttpServletResponse response) throws IOException {
        List<Schedule> schedules = scheduleRepo.findAll();
        ExcelExportUtils exportUtils = new ExcelExportUtils(schedules);
        exportUtils.exportDataToExcel(response);
        return schedules;
    }
}