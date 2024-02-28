package healthcheck.serviceTest;

import healthcheck.dto.Application.request.ApplicationProcessedRequest;
import healthcheck.dto.Application.request.ApplicationRequest;
import healthcheck.dto.Application.response.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.entities.Application;
import healthcheck.exceptions.NotFoundException;
import healthcheck.repo.ApplicationRepo;
import healthcheck.repo.Dao.ApplicationDao;
import healthcheck.service.Impl.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
public class ApplicationServiceTest {
    @Mock
    private ApplicationRepo applicationRepo;

    @Mock
    private ApplicationDao applicationDao;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateApplication_Success() {
        ApplicationRequest applicationRequest = new ApplicationRequest("testUser", "1234567890");
        when(applicationRepo.save(any(Application.class))).thenReturn(new Application());

        SimpleResponse response = applicationService.createApplication(applicationRequest);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Заявка успешно отправлена!", response.getMessage());
    }

    @Test
    void testCreateApplication_Failure() {
        ApplicationRequest applicationRequest = new ApplicationRequest("testUser", "1234567890");
        when(applicationRepo.save(any(Application.class))).thenThrow(new RuntimeException("Error"));

        SimpleResponse response = applicationService.createApplication(applicationRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
        assertEquals("Произошла ошибка.", response.getMessage());
    }

    @Test
    void testGetApplications() {
        List<ApplicationResponse> expectedResponses = Arrays.asList(
                new ApplicationResponse(1L, "User1", LocalDate.now(), "1234567890", true),
                new ApplicationResponse(2L, "User2", LocalDate.now(), "0987654321", false)
        );
        when(applicationDao.getApplications(anyString())).thenReturn(expectedResponses);

        List<ApplicationResponse> responses = applicationService.getApplications("test");

        assertEquals(expectedResponses.size(), responses.size());
        assertEquals(expectedResponses.get(0).getUsername(), responses.get(0).getUsername());
        assertEquals(expectedResponses.get(1).getUsername(), responses.get(1).getUsername());
    }

    @Test
    void testGetAllApplications() {
        List<ApplicationResponse> expectedResponses = Arrays.asList(
                new ApplicationResponse(1L, "User1", LocalDate.now(), "1234567890", true),
                new ApplicationResponse(2L, "User2", LocalDate.now(), "0987654321", false)
        );
        when(applicationDao.getAllApplications()).thenReturn(expectedResponses);

        List<ApplicationResponse> responses = applicationService.getAllApplications();

        assertEquals(expectedResponses.size(), responses.size());
        assertEquals(expectedResponses.get(0).getUsername(), responses.get(0).getUsername());
        assertEquals(expectedResponses.get(1).getUsername(), responses.get(1).getUsername());
    }

    @Test
    void testDeleteAll() {
        List<Long> request = Arrays.asList(1L, 2L);
        when(applicationRepo.findAllById(request)).thenReturn(new ArrayList<>());

        SimpleResponse response = applicationService.deleteAll(request);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Successfully deleted applications", response.getMessage());
    }

    @Test
    void testDeleteAll_NotFound() {
        List<Long> request = Arrays.asList(1L, 2L);
        doThrow(EmptyResultDataAccessException.class).when(applicationRepo).findAllById(request);

        SimpleResponse response = applicationService.deleteAll(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
        assertEquals("Error deleting applications: Some applications not found", response.getMessage());
    }

    @Test
    void testDeleteAll_Error() {
        List<Long> request = Arrays.asList(1L, 2L);
        doThrow(new RuntimeException()).when(applicationRepo).findAllById(request);

        SimpleResponse response = applicationService.deleteAll(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
        assertEquals("Error deleting applications: ", response.getMessage());
    }

    @Test
    void testProcessedById_Success() {
        ApplicationProcessedRequest request = new ApplicationProcessedRequest(true, 1L);
        Application application = new Application();
        when(applicationRepo.findById(1L)).thenReturn(Optional.of(application));

        boolean processed = applicationService.processedById(request);

        assertTrue(processed);
    }

    @Test
    void testProcessedById_NotFound() {
        ApplicationProcessedRequest request = new ApplicationProcessedRequest(true, 1L);
        when(applicationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> applicationService.processedById(request));
    }

    @Test
    void testDeleteById_Success() {
        Application application = new Application();
        application.setProcessed(true);
        when(applicationRepo.findById(1L)).thenReturn(Optional.of(application));

        SimpleResponse response = applicationService.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Successfully deleted", response.getMessage());
    }

    @Test
    void testDeleteById_NotProcessed() {
        Application application = new Application();
        application.setProcessed(false);
        when(applicationRepo.findById(1L)).thenReturn(Optional.of(application));

        SimpleResponse response = applicationService.deleteById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
        assertEquals("Error deleting application: Application not processed", response.getMessage());
    }

    @Test
    void testDeleteById_NotFound() {
        when(applicationRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> applicationService.deleteById(1L));
    }
}

