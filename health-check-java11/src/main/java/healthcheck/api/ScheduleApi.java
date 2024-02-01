package healthcheck.api;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ScheduleGetResponse;
import healthcheck.dto.Schedule.ScheduleUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Doctor;
import healthcheck.enums.Facility;
import healthcheck.service.DoctorService;
import healthcheck.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleApi {

    private final ScheduleService scheduleService;
    private final DoctorService doctorService;

    @GetMapping("/getDoctorsByDepartment")
    @Operation(summary = "Get Doctors by Department", description = "Retrieve a list of doctors based on the specified facility.")
    @PostAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<Doctor> getDoctorsByDepartment(@RequestParam Facility facility) {
        return doctorService.getDoctorsByDepartment(facility);
    }

    @PostMapping("/saveScheduleDoctor")
    @Operation(summary = "Save Doctor Schedule", description = "Save the schedule for a specific doctor in the given facility.")
    @PostAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse saveScheduleDoctor(@RequestParam Facility facility, @RequestParam Long doctorId,
                                             @RequestBody AddScheduleRequest addScheduleRequest) {
        return scheduleService.saveSchedule(facility, doctorId, addScheduleRequest);
    }

    @PatchMapping("/update-time-sheet-doctor")
    @Operation(summary = "Обновить расписание для доктора")
    @PostAuthorize("hasAuthority('ADMIN')")
    public ScheduleGetResponse updateTimeSheetDoctor(@RequestParam Long doctorId, @RequestParam LocalDate date,
                                                     @RequestBody ScheduleUpdateRequest scheduleUpdateRequest) {
        List<ScheduleUpdateRequest.TimeSlot> timeSlots = scheduleUpdateRequest.getTimeSlots();
        return scheduleService.updateScheduleByDoctorId(doctorId, date, timeSlots);
    }
}