package healthcheck.api;

import healthcheck.dto.Appointment.*;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.enums.Facility;
import healthcheck.service.AppointmentService;
import healthcheck.service.TimeSheetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentApi {

    private final AppointmentService appointmentService;
    private final TimeSheetService timeSheetService;

    @GetMapping("/getAppointment")
    @Operation(summary = "Get appointment", description = "Endpoint to get appointment.")
    @PostAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentResponse> getAppointment(String word){
        return appointmentService.getAllAppointment(word);
    }

    @GetMapping("/confirmation")
    @PostAuthorize("hasAnyAuthority('USER')")
    public void buildAppointmentConfirmationEmail() {
        appointmentService.buildAppointmentConfirmationEmail();
    }

    @PostMapping("/add")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "add appointment", description = "Endpoint to add appointment.")
    public OnlineAppointmentResponse addAppointment(@RequestParam Facility facility, @Valid @RequestBody AppointmentRequest request) throws MessagingException, IOException {
        return appointmentService.addAppointment(facility,request);
    }
    @PatchMapping()
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "check verification code", description = "Endpoint checking code appointment.")
    public SimpleResponse checkVerificationCode(@RequestParam Long appointmentId, @RequestParam String code) {
        return appointmentService.verifyAppointment(appointmentId,code);
    }

    @DeleteMapping()
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "delete appointment", description = "Endpoint to delete appointment.")
    public SimpleResponse delete(@RequestParam Long appointmentId) {
        return appointmentService.deleteAppointment(appointmentId);
    }

    @GetMapping("/getTimesheet")
    @Operation(summary = "Get available timesheet doctors", description = "Endpoint to get available timesheet doctors.")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<TimeSheetResponse> getTimeSheetDoctors(@RequestParam String facility) {
        return timeSheetService.getTimesheetDoctor(facility);
    }

    @GetMapping("/getDoctor")
    @Operation(summary = "Get doctor for appointment", description = "Endpoint to get doctor for appointment.")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    public FindByDoctorForAppointment findByDoctorId(@RequestParam Long id){
        return appointmentService.findByDoctorId(id);
    }
    @GetMapping("/{id}/getDoctorSchedule")
    @Operation(summary = "Get doctor for appointment", description = "Endpoint to get doctor for appointment.")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<AppointmentScheduleTimeSheetResponse>getTheDoctorFreeTimeInTheCalendar(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @PathVariable Long id
    ){
        return appointmentService.getTheDoctorFreeTimeInTheCalendar(startDate,endDate,id);
    }
}
