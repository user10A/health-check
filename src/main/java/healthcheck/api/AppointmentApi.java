package healthcheck.api;

import healthcheck.dto.Appointment.*;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.enums.Facility;
import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.service.AppointmentService;
import healthcheck.service.TimeSheetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentResponse> getAppointment(String word){
        return appointmentService.getAllAppointment(word);
    }

    @GetMapping("/confirmation")
    @Operation(summary = "Appointment Confirmation Email", description = "This API is used to send an appointment confirmation email. Requires USER authority.")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse appointmentConfirmationEmail() {
        return appointmentService.appointmentConfirmationEmail();
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

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Delete appointment",
            description = "This method can be used by both administrators and patients")
    public SimpleResponse delete(@RequestBody(required = false) List<AppointmentDeleteRequest> appointments) {
        return appointmentService.deleteAllAppointmentById(appointments);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all appointments",
            description = "Retrieve a list of all appointments with default settings.")
    public List<AppointmentResponse> getAllAppointmentsDefault() {
        return appointmentService.getAllAppointmentDefault();
    }

    @DeleteMapping("/deleteById")
    @Operation(summary = "Delete appointment by ID",
            description = "Delete an appointment by providing the appointment ID. " +
                    "The appointment must be inactive to be deleted.")
    public SimpleResponse deleteAppointmentById(@RequestBody AppointmentDeleteRequest appointmentDeleteRequest) {
        return appointmentService.deleteAppointmentById(appointmentDeleteRequest);
    }
}
