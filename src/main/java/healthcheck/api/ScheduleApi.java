package healthcheck.api;

import healthcheck.dto.Appointment.AddScheduleRequest;
import healthcheck.dto.Schedule.ResponseToGetSchedules;
import healthcheck.dto.Schedule.ScheduleGetResponse;
import healthcheck.dto.Schedule.ScheduleUpdateRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Doctor;
import healthcheck.enums.Facility;
import healthcheck.exceptions.NotFoundException;
import healthcheck.service.DoctorService;
import healthcheck.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@CrossOrigin
@Tag(name = "Schedule api", description = "API's for schedules ")
public class ScheduleApi {

    private final ScheduleService scheduleService;
    private final DoctorService doctorService;

    @GetMapping("/getDoctorsByDepartment")
    @Operation(summary = "Get Doctors by Department", description = "Retrieve a list of doctors based on the specified facility.")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<Doctor> getDoctorsByDepartment(@RequestParam Facility facility) {
        return doctorService.getDoctorsByDepartment(facility);
    }

    @PostMapping("/saveScheduleDoctor")
    @Operation(summary = "Save Doctor Schedule", description = "Save the schedule for a specific doctor in the given facility.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse saveScheduleDoctor(@RequestParam Facility facility, @RequestParam Long doctorId,
                                             @RequestBody AddScheduleRequest addScheduleRequest) {
        return scheduleService.saveSchedule(facility, doctorId, addScheduleRequest);
    }

    @PatchMapping("/update-time-sheet-doctor")
    @Operation(summary = "Update schedule for a doctor")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ScheduleGetResponse updateTimeSheetDoctor(@RequestParam Long doctorId, @RequestParam LocalDate date,
                                                     @RequestBody List<ScheduleUpdateRequest> scheduleUpdateRequest) {
        return scheduleService.updateScheduleByDoctorId(doctorId, date, scheduleUpdateRequest);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all schedules")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ResponseToGetSchedules>> getAllSchedules() {
        List<ResponseToGetSchedules> schedules = scheduleService.getAllSchedules();
        if (schedules.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/getByDate")
    @Operation(summary = "Get schedules by date range")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ResponseToGetSchedules>> getScheduleByDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        try {
            List<ResponseToGetSchedules> schedules = scheduleService.getScheduleByDate(startDate, endDate);
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/search")
    @Operation(summary = "Search schedules by keyword")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ResponseToGetSchedules>> getScheduleBySearch(
            @RequestParam String word,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        try {
            List<ResponseToGetSchedules> schedules = scheduleService.getScheduleBySearch(word,start,end);
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/export-to-excel")
    @Operation(summary = "export schedule to excel")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Doctors Schedule.xlsx";
        response.setHeader(headerKey, headerValue);
        scheduleService.exportCustomerToExcel(response);

    }
}