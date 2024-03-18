package healthcheck.serviceTest;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.Appointment.AppointmentScheduleTimeSheetResponse;
import healthcheck.dto.Appointment.FindDoctorForAppointmentResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Appointment;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import healthcheck.entities.TimeSheet;
import healthcheck.enums.Facility;
import healthcheck.enums.Status;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.AppointmentRepo;
import healthcheck.repo.Dao.AppointmentDao;
import healthcheck.repo.DoctorRepo;
import healthcheck.repo.TimeSheetRepo;
import healthcheck.service.Impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {


    @Mock
    private AppointmentRepo appointmentRepo;
    @Mock
    private AppointmentDao appointmentDao;
    @Mock
    private DoctorRepo doctorRepo;
    @Mock
    private TimeSheetRepo timeSheetRepo;


    @InjectMocks
    private AppointmentServiceImpl appointmentService;

//    @Test
//    public void testVerifyAppointment_CorrectVerificationCode() {
//        Long appointmentId = 1L;
//        String verificationCode = "123456";
//        Appointment appointment = new Appointment();
//        appointment.setId(appointmentId);
//        appointment.setVerificationCode(verificationCode);
//        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));
////        SimpleResponse response = appointmentService.verifyAppointment(appointmentId, verificationCode);
//        assertNotNull(response);
//        assertEquals("Пациент успешно записан", response.getMessage());
//        assertEquals(HttpStatus.OK, response.getHttpStatus());
//        assertEquals(Status.FINISHED, appointment.getStatus());
//        assertNull(appointment.getVerificationCode());
//        verify(appointmentRepo, times(1)).findById(appointmentId);
//        verify(appointmentRepo, times(1)).save(appointment);
//    }

//    @Test
//    public void testVerifyAppointment_IncorrectVerificationCode() {
//        Long appointmentId = 1L;
//        String verificationCode = "654321";
//        Appointment appointment = new Appointment();
//        appointment.setId(appointmentId);
//        appointment.setVerificationCode("123456");
//        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));
//        SimpleResponse response = appointmentService.verifyAppointment(appointmentId, verificationCode);
//
//        assertNotNull(response);
//        assertEquals("Не правильный код регистрации", response.getMessage());
//        assertEquals(HttpStatus.CONFLICT, response.getHttpStatus());
//        assertEquals("123456", appointment.getVerificationCode());
//        verify(appointmentRepo, times(1)).findById(appointmentId);
//        verify(appointmentRepo, never()).save(any(Appointment.class));
//    }

    @Test
    public void testVerifyAppointment_NonExistingAppointment() {
        Long appointmentId = 1L;
        String verificationCode = "123456";
        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.verifyAppointment(appointmentId, verificationCode));
        verify(appointmentRepo, times(1)).findById(appointmentId);
        verify(appointmentRepo, never()).save(any(Appointment.class));
    }

    @Test
    public void testDeleteAllAppointmentsById_SuccessfulDeletion() {
        List<Long> appointmentIds = Arrays.asList(1L, 2L, 3L);
        List<Appointment> appointments = new ArrayList<>();
        when(appointmentRepo.findAllById(appointmentIds)).thenReturn(appointments);

        SimpleResponse response = appointmentService.deleteAllAppointmentsById(appointmentIds);

        verify(appointmentRepo).deleteAll(appointments);
//        assertEquals("Заявки успешно удалены", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    public void testDeleteAllAppointmentsById_AppointmentsNotFound() {
        List<Long> appointmentIds = Arrays.asList(1L, 2L, 3L);
        when(appointmentRepo.findAllById(appointmentIds)).thenThrow(RuntimeException.class);

        SimpleResponse response = appointmentService.deleteAllAppointmentsById(appointmentIds);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());

    }

    @Test
    public void testDeleteAllAppointmentsById_ExceptionThrown() {
        List<Long> appointmentIds = Arrays.asList(1L, 2L, 3L);
        when(appointmentRepo.findAllById(appointmentIds)).thenThrow(RuntimeException.class);

        SimpleResponse response = appointmentService.deleteAllAppointmentsById(appointmentIds);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());

    }

    @Test
    public void testDeleteAppointmentById_SuccessfulDeletion() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setProcessed(true);

        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));

        SimpleResponse response = appointmentService.deleteAppointmentById(appointmentId);

        verify(appointmentRepo).delete(appointment);
//        assertEquals("Успешно удален", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    public void testDeleteAppointmentById_NotProcessedAppointment() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setProcessed(false);

        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));

        SimpleResponse response = appointmentService.deleteAppointmentById(appointmentId);

        verify(appointmentRepo, never()).delete(any(Appointment.class));
//        assertEquals("Ошибка Appointment с ID: 1 не обработан", response.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
    }

    @Test
    public void testDeleteAppointmentById_AppointmentNotFound() {
        Long appointmentId = 1L;
        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.deleteAppointmentById(appointmentId));
        verify(appointmentRepo, never()).delete(any(Appointment.class));
    }

    @Test
    public void testGetAllAppointmentDefault() {
        List<AppointmentResponse> expectedAppointments = Arrays.asList(
                new AppointmentResponse(),
                new AppointmentResponse()
        );
        when(appointmentDao.getAllAppointmentDefault()).thenReturn(expectedAppointments);

        List<AppointmentResponse> actualAppointments = appointmentService.getAllAppointmentDefault();

        assertNotNull(actualAppointments);
        assertEquals(expectedAppointments.size(), actualAppointments.size());
        assertEquals(expectedAppointments.get(0).getAppointmentId(), actualAppointments.get(0).getAppointmentId());
        assertEquals(expectedAppointments.get(0).getFullName(), actualAppointments.get(0).getFullName());
        assertEquals(expectedAppointments.get(1).getAppointmentId(), actualAppointments.get(1).getAppointmentId());
        assertEquals(expectedAppointments.get(1).getFullName(), actualAppointments.get(1).getFullName());
        verify(appointmentDao, times(1)).getAllAppointmentDefault();
    }

    @Test
    public void testGetTheDoctorFreeTimeInTheCalendar() {
        // Arrange
        String startDate = "2022-01-01";
        String endDate = "2022-01-31";
        Long doctorId = 1L;
        List<AppointmentScheduleTimeSheetResponse> expectedTimeSheet = Arrays.asList(
                new AppointmentScheduleTimeSheetResponse("2022-01-01", "09:00", "10:00"),
                new AppointmentScheduleTimeSheetResponse("2022-01-01", "10:00", "11:00"),
                new AppointmentScheduleTimeSheetResponse("2022-01-02", "14:00", "15:00")
        );
        when(appointmentDao.getTheDoctorFreeTimeInTheCalendar(startDate, endDate, doctorId)).thenReturn(expectedTimeSheet);

        // Act
        List<AppointmentScheduleTimeSheetResponse> actualTimeSheet = appointmentService.getTheDoctorFreeTimeInTheCalendar(startDate, endDate, doctorId);

        // Assert

        assertNotNull(actualTimeSheet);
        assertEquals(expectedTimeSheet.size(), actualTimeSheet.size());
        assertEquals(expectedTimeSheet.get(0).getDateOfConsultation(), actualTimeSheet.get(0).getDateOfConsultation());
        assertEquals(expectedTimeSheet.get(0).getStartTimeOfConsultation(), actualTimeSheet.get(0).getStartTimeOfConsultation());
        assertEquals(expectedTimeSheet.get(0).getStartTimeOfConsultation(), actualTimeSheet.get(0).getStartTimeOfConsultation());
        assertEquals(expectedTimeSheet.get(1).getDateOfConsultation(), actualTimeSheet.get(1).getDateOfConsultation());
        assertEquals(expectedTimeSheet.get(1).getStartTimeOfConsultation(), actualTimeSheet.get(1).getStartTimeOfConsultation());
        assertEquals(expectedTimeSheet.get(1).getStartTimeOfConsultation(), actualTimeSheet.get(1).getStartTimeOfConsultation());
        verify(appointmentDao, times(1)).getTheDoctorFreeTimeInTheCalendar(startDate, endDate, doctorId);
    }

    @Test
    public void testFindByDoctorId_ExistingDoctor() {
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setImage("doctor_image.jpg");
        doctor.setFirstName("Dr. John Doe");
        Department department = new Department();
        Facility facility = Facility.Кардиология;
        department.setFacility(facility);
        doctor.setDepartment(department);

        when(doctorRepo.findById(doctorId)).thenReturn(Optional.of(doctor));

        FindDoctorForAppointmentResponse response = appointmentService.findByDoctorId(doctorId);

        assertNotNull(response);
        assertEquals(doctor.getImage(), response.getImage());
        assertEquals(doctor.getFullNameDoctor(), response.getFullNameDoctor());
        assertEquals(doctor.getDepartment().getFacility().name(), response.getFacility());
        verify(doctorRepo, times(1)).findById(doctorId);
    }

    @Test
    public void testFindByDoctorId_NonExistingDoctor() {
        Long doctorId = 1L;
        when(doctorRepo.findById(doctorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.findByDoctorId(doctorId));
        verify(doctorRepo, times(1)).findById(doctorId);
    }

    @Test
    public void testDeleteAppointment_ExistingAppointment() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setDoctor(new Doctor());
        appointment.getDoctor().setId(2L);
        appointment.setAppointmentDate(LocalDate.parse("2022-01-01"));
        appointment.setAppointmentTime(LocalTime.parse("09:00"));

        TimeSheet timeSheet = new TimeSheet();
        timeSheet.setId(appointment.getDoctor().getId());
        timeSheet.setStartTimeOfConsultation((appointment.getAppointmentTime()));
        timeSheet.setAvailable(true);

        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(timeSheetRepo.getTimeSheetByDoctorIdAndStartTime(appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime())).thenReturn(timeSheet);

        SimpleResponse response = appointmentService.deleteAppointment(appointmentId);

        assertNotNull(response);
//        assertEquals("успешно удален ", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        verify(appointmentRepo, times(1)).findById(appointmentId);
        verify(appointmentRepo, times(1)).delete(appointment);
        verify(timeSheetRepo, times(1)).save(timeSheet);
    }

    @Test
    public void testDeleteAppointment_NonExistingAppointment() {
        Long appointmentId = 1L;
        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.deleteAppointment(appointmentId));
        verify(appointmentRepo, times(1)).findById(appointmentId);
        verify(appointmentRepo, never()).delete(any(Appointment.class));
        verify(timeSheetRepo, never()).save(any(TimeSheet.class));
    }

    @Test
    public void testGenerateVerificationCode() {
        String verificationCode = appointmentService.generateVerificationCode();

        assertNotNull(verificationCode);
        assertEquals(6, verificationCode.length());
        assertTrue(verificationCode.matches("\\d{6}"));
    }

    @Test
    public void testGetAllAppointment() {
        String word = "test";

        List<AppointmentResponse> expectedAppointments = Arrays.asList(
                new AppointmentResponse(),
                new AppointmentResponse()
        );

        when(appointmentDao.getAllAppointment(word)).thenReturn(expectedAppointments);

        List<AppointmentResponse> actualAppointments = appointmentService.getAllAppointment(word);

        assertEquals(expectedAppointments, actualAppointments);
        verify(appointmentDao, times(1)).getAllAppointment(word);
    }
}