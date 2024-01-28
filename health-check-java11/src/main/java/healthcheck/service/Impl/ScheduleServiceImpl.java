package healthcheck.service.Impl;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import healthcheck.entities.Schedule;
import healthcheck.entities.TimeSheet;
import healthcheck.enums.DaysOfRepetition;
import healthcheck.enums.Facility;
import healthcheck.exceptions.AlreadyExistsException;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.DoctorRepo;
import healthcheck.repo.ScheduleRepo;
import healthcheck.repo.TimeSheetRepo;
import healthcheck.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final DoctorRepo doctorRepo;
    private final DepartmentRepo departmentRepo;
    private final ScheduleRepo scheduleRepo;
    private final TimeSheetRepo timeSheetRepo;

    @Override
    public SimpleResponse saveAppointment(@NonNull Facility facility, @NonNull Long doctorId,
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
}