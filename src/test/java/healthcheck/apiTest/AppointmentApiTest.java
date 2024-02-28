package healthcheck.apiTest;
import healthcheck.api.AppointmentApi;
import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.Appointment.AppointmentRequest;
import healthcheck.dto.Appointment.OnlineAppointmentResponse;
import healthcheck.dto.Appointment.FindDoctorForAppointmentResponse;
import healthcheck.dto.Appointment.AppointmentScheduleTimeSheetResponse;
import healthcheck.dto.Appointment.AppointmentProcessedRequest;
import healthcheck.dto.SimpleResponse;
import healthcheck.dto.TimeSheet.TimeSheetResponse;
import healthcheck.enums.Facility;
import healthcheck.service.AppointmentService;
import healthcheck.service.TimeSheetService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppointmentApiTest {
    @Mock
    private AppointmentService appointmentService;

    @Mock
    private TimeSheetService timeSheetService;

    private AppointmentApi appointmentApi;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        appointmentApi = new AppointmentApi(appointmentService, timeSheetService);
    }

    @Test
    public void testGetAppointment() {
        String word = "test";
        List<AppointmentResponse> expectedResponse = Arrays.asList(new AppointmentResponse(), new AppointmentResponse());

        Mockito.when(appointmentService.getAllAppointment(word)).thenReturn(expectedResponse);

        List<AppointmentResponse> response = appointmentApi.getAppointment(word);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testAddAppointment() throws MessagingException, IOException {
        Facility facility = Facility.Анестезиология;
        AppointmentRequest request = new AppointmentRequest();
        OnlineAppointmentResponse expectedResponse = new OnlineAppointmentResponse();
        Mockito.when(appointmentService.addAppointment(facility, request)).thenReturn(expectedResponse);
        OnlineAppointmentResponse response = appointmentApi.addAppointment(facility, request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testAddAppointmentByDoctorId() throws MessagingException, IOException {
        AppointmentRequest request = new AppointmentRequest();
        OnlineAppointmentResponse expectedResponse = new OnlineAppointmentResponse();
        Mockito.when(appointmentService.addAppointmentByDoctorId(request)).thenReturn(expectedResponse);
        OnlineAppointmentResponse response = appointmentApi.addAppointmentByDoctorId(request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testCheckVerificationCode() {
        Long appointmentId = 1L;
        String code = "123456";
        SimpleResponse expectedResponse = new SimpleResponse(true, "Verification code checked successfully");
        Mockito.when(appointmentService.appointmentConfirmationEmail(appointmentId)).thenReturn(expectedResponse);
        Mockito.when(appointmentService.verifyAppointment(appointmentId, code)).thenReturn(expectedResponse);
        SimpleResponse response = appointmentApi.checkVerificationCode(appointmentId, code);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDelete() {
        Long appointmentId = 1L;
        SimpleResponse expectedResponse = new SimpleResponse(true, "Appointment deleted successfully");
        Mockito.when(appointmentService.deleteAppointment(appointmentId)).thenReturn(expectedResponse);
        SimpleResponse response = appointmentApi.delete(appointmentId);
        assertEquals(expectedResponse, response);
    }


    @Test
    public void testGetTimeSheetDoctors() {
        String facility = "hospital";
        List<TimeSheetResponse> expectedResponse = Arrays.asList(new TimeSheetResponse(), new TimeSheetResponse());
        Mockito.when(timeSheetService.getTimesheetDoctor(Mockito.eq(facility))).thenReturn(expectedResponse);
        List<TimeSheetResponse> response = appointmentApi.getTimeSheetDoctors(facility);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testFindByDoctorId() {
        Long id = 1L;
        FindDoctorForAppointmentResponse expectedResponse = new FindDoctorForAppointmentResponse();
        Mockito.when(appointmentService.findByDoctorId(Mockito.eq(id))).thenReturn(expectedResponse);
        FindDoctorForAppointmentResponse response = appointmentApi.findByDoctorId(id);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetTheDoctorFreeTimeInTheCalendar() {
        String startDate = "2024-02-01";
        String endDate = "2024-02-28";
        Long id = 1L;
        List<AppointmentScheduleTimeSheetResponse> expectedResponse = Arrays.asList(
                new AppointmentScheduleTimeSheetResponse(),
                new AppointmentScheduleTimeSheetResponse()
        );
        Mockito.when(appointmentService.getTheDoctorFreeTimeInTheCalendar(startDate, endDate, id))
                .thenReturn(expectedResponse);
        List<AppointmentScheduleTimeSheetResponse> response = appointmentApi.getTheDoctorFreeTimeInTheCalendar(startDate, endDate, id);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDeleteAll() {
        List<Long> appointments = Arrays.asList(1L, 2L, 3L);
        SimpleResponse expectedResponse = new SimpleResponse(true, "All appointments deleted successfully");
        Mockito.when(appointmentService.deleteAllAppointmentsById(appointments))
                .thenReturn(expectedResponse);
        SimpleResponse response = appointmentApi.delete(appointments);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetAllAppointmentsDefault() {
        List<AppointmentResponse> expectedResponse = Arrays.asList(
                new AppointmentResponse(),
                new AppointmentResponse(),
                new AppointmentResponse()
        );
        Mockito.when(appointmentService.getAllAppointmentDefault()).thenReturn(expectedResponse);
        List<AppointmentResponse> response = appointmentApi.getAllAppointmentsDefault();
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDeleteAppointmentById() {
        Long id = 1L;
        SimpleResponse expectedResponse = new SimpleResponse(true, "Appointment deleted successfully");
        Mockito.when(appointmentService.deleteAppointmentById(id)).thenReturn(expectedResponse);
        SimpleResponse response = appointmentApi.deleteAppointmentById(id);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testProcessed() {
        AppointmentProcessedRequest processed = new AppointmentProcessedRequest();
        boolean expectedResponse = true;
        Mockito.when(appointmentService.updateProcessed(processed)).thenReturn(expectedResponse);
        boolean response = appointmentApi.processed(processed);
        assertTrue(response);
    }
}