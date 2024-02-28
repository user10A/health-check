package healthcheck.daoTest;

import healthcheck.dto.Application.response.ApplicationResponse;
import healthcheck.repo.Dao.Impl.ApplicationDaoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplicationDaoTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    private ApplicationDaoImpl applicationDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationDao = new ApplicationDaoImpl(jdbcTemplate);
    }

    @Test
    void testGetApplications() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(getMockApplicationResponses());

        List<ApplicationResponse> applications = applicationDao.getApplications("test");

        Assertions.assertEquals(2, applications.size());
        Assertions.assertEquals("John Doe", applications.get(0).getUsername());
        Assertions.assertEquals(LocalDate.now(), applications.get(0).getDateOfApplicationCreation());
        Assertions.assertEquals("1234567890", applications.get(0).getPhoneNumber());
        Assertions.assertTrue(applications.get(0).isProcessed());

        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetAllApplications() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(getMockApplicationResponses());

        List<ApplicationResponse> applications = applicationDao.getAllApplications();

        Assertions.assertEquals(2, applications.size());
        Assertions.assertEquals("John Doe", applications.get(0).getUsername());
        Assertions.assertEquals(LocalDate.now(), applications.get(0).getDateOfApplicationCreation());
        Assertions.assertEquals("1234567890", applications.get(0).getPhoneNumber());
        Assertions.assertTrue(applications.get(0).isProcessed());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }
    private List<ApplicationResponse> getMockApplicationResponses() {
        ApplicationResponse application1 = new ApplicationResponse(1, "John Doe", LocalDate.now(), "1234567890", true);
        ApplicationResponse application2 = new ApplicationResponse(2, "Jane Smith", LocalDate.now(), "9876543210", false);
        return Arrays.asList(application1, application2);
    }
}



