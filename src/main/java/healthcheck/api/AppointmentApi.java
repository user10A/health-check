package healthcheck.api;

import healthcheck.dto.Appointment.AppointmentScheduleTimeSheetResponse;
import healthcheck.dto.Appointment.OnlineAppointmentResponse;
import healthcheck.dto.Appointment.AppointmentRequest;
import healthcheck.dto.Appointment.AppointmentProcessedRequest;
import healthcheck.dto.Appointment.FindDoctorForAppointmentResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.enums.Facility;
import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.service.AppointmentService;
import healthcheck.service.TimeSheetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
@CrossOrigin
@Tag(name = "Appointment api", description = "API's for appointments ")
@Slf4j
public class AppointmentApi {

    private final AppointmentService appointmentService;
    private final TimeSheetService timeSheetService;

    @GetMapping("/getAppointment")
    @Operation(summary = "Get appointment", description = "Endpoint to get appointment.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppointmentResponse> getAppointment(@RequestParam String word){
        return appointmentService.getAllAppointment(word);
    }

    @PostMapping("/add")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "add appointment", description = "Endpoint to add appointment.")
    public OnlineAppointmentResponse addAppointment(@RequestParam Facility facility, @Valid @RequestBody AppointmentRequest request) throws MessagingException, IOException {
        return appointmentService.addAppointment(facility,request);
    }
    @PostMapping("/addByDoctorId")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "add appointment", description = "Endpoint to add appointment.")
    public OnlineAppointmentResponse addAppointmentByDoctorId(@RequestBody AppointmentRequest request) throws MessagingException, IOException {
        return appointmentService.addAppointmentByDoctorId(request);
    }

    @PatchMapping()
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "check verification code", description = "Endpoint checking code appointment.")
    public SimpleResponse checkVerificationCode(@RequestParam Long appointmentId, @RequestParam String code) {
        SimpleResponse result=appointmentService.appointmentConfirmationEmail(appointmentId);
        log.info(String.valueOf(result));
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

    @GetMapping("/{id}")
    @Operation(summary = "Get available timesheet doctors by ID", description = "Endpoint to get available timesheet doctors by doctor ID.")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<TimeSheetResponse> getTimeSheetDoctors(@PathVariable Long id) {
        return timeSheetService.getTimesheetDoctorById(id);
    }

    @GetMapping("/getDoctor")
    @Operation(summary = "Get doctor for appointment", description = "Endpoint to get doctor for appointment.")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    public FindDoctorForAppointmentResponse findByDoctorId(@RequestParam Long id){
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

    @DeleteMapping("/deleteAll") //delete all
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Delete appointment",
            description = "This method can be used by both administrators and patients")
    public SimpleResponse delete(@RequestBody List<Long> appointments) {
        return appointmentService.deleteAllAppointmentsById(appointments);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all appointments",
            description = "Retrieve a list of all appointments with default settings.")
    public List<AppointmentResponse> getAllAppointmentsDefault() {
        return appointmentService.getAllAppointmentDefault();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete appointment by ID",
            description = "Delete an appointment by providing the appointment ID. " +
                    "The appointment must be inactive to be deleted.")
    public SimpleResponse deleteAppointmentById(@PathVariable Long id) {
        return appointmentService.deleteAppointmentById(id);
    }
    @PutMapping("update")
    @Operation(summary = "update processed ", description = "Endpoint to update processed by id (Admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean processed(@RequestBody AppointmentProcessedRequest processed){
        return appointmentService.updateProcessed(processed);
    }
}