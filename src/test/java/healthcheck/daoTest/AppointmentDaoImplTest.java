package healthcheck.daoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import healthcheck.dto.Appointment.AppointmentResponse;
import healthcheck.dto.Appointment.AppointmentScheduleTimeSheetResponse;
import healthcheck.repo.Dao.Impl.AppointmentDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import static org.mockito.Mockito.any;
public class AppointmentDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AppointmentDaoImpl appointmentDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
//   1 method


    @Test
    public void testGetTheDoctorFreeTimeInTheCalendar_EmptySchedule() {
        String startDate = "2024-03-01";
        String endDate = "2024-03-10";
        Long doctorId = 1L;

        when(jdbcTemplate.query(any(String.class), any(Object[].class), (ResultSetExtractor<Object>) any()))
                .thenReturn(new ArrayList<>()); // Mocking an empty result set

        List<AppointmentScheduleTimeSheetResponse> actualResponse = appointmentDao.getTheDoctorFreeTimeInTheCalendar(startDate, endDate, doctorId);

        assertEquals(10, actualResponse.size()); // Here, we expect the full schedule because there are no appointments
    }


    @Test
    public void testGetAllAppointment() {
        String word = "John";
        List<AppointmentResponse> expectedResponse = Arrays.asList(
                new AppointmentResponse(1L, "John Doe", "1234567890", "john@example.com", "Facility 1", "Doctor 1", LocalDate.now(), LocalTime.now(), "Scheduled", true),
                new AppointmentResponse(2L, "Jane Doe", "0987654321", "jane@example.com", "Facility 2", "Doctor 2", LocalDate.now(), LocalTime.now(), "Scheduled", true)
        );

        // Mock ResultSet
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(RowMapper.class)))
                .thenReturn(expectedResponse);

        List<AppointmentResponse> actualResponse = appointmentDao.getAllAppointment(word);

        assertEquals(expectedResponse.size(), actualResponse.size());
    }

    @Test
    public void testGetAllAppointmentDefault() {
        List<AppointmentResponse> expectedResponse = Arrays.asList(
                new AppointmentResponse(1L, "John Doe", "1234567890", "john@example.com", "Facility 1", "Doctor 1", LocalDate.now(), LocalTime.now(), "Scheduled", true),
                new AppointmentResponse(2L, "Jane Doe", "0987654321", "jane@example.com", "Facility 2", "Doctor 2", LocalDate.now(), LocalTime.now(), "Scheduled", true)
        );

        when(jdbcTemplate.query(any(String.class), any(RowMapper.class)))
                .thenReturn(expectedResponse);

        List<AppointmentResponse> actualResponse = appointmentDao.getAllAppointmentDefault();

        assertEquals(expectedResponse.size(), actualResponse.size());
    }
}
