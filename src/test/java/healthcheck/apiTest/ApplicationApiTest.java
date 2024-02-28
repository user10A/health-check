package healthcheck.apiTest;

import healthcheck.api.ApplicationApi;
import healthcheck.dto.Application.request.ApplicationProcessedRequest;
import healthcheck.dto.Application.request.ApplicationRequest;
import healthcheck.dto.Application.response.ApplicationResponse;
import healthcheck.dto.SimpleResponse;
import healthcheck.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationApiTest {
    @Mock
    private ApplicationService applicationService;

    private ApplicationApi applicationApi;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationApi = new ApplicationApi(applicationService);
    }

    @Test
    public void testCreateApplication() {
        ApplicationRequest request = new ApplicationRequest();
        SimpleResponse expectedResponse = new SimpleResponse(true, "Application created successfully");
        Mockito.when(applicationService.createApplication(request)).thenReturn(expectedResponse);
        SimpleResponse response = applicationApi.createApplication(request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetApplication() {
        String word = "test";
        List<ApplicationResponse> expectedResponse = Collections.singletonList(new ApplicationResponse());
        Mockito.when(applicationService.getApplications(word)).thenReturn(expectedResponse);
        List<ApplicationResponse> response = applicationApi.getApplication(word);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        SimpleResponse expectedResponse = new SimpleResponse(true, "Application deleted successfully");
        Mockito.when(applicationService.deleteById(id)).thenReturn(expectedResponse);
        SimpleResponse response = applicationApi.deleteById(id);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDeleteAll() {
        List<Long> listId = Arrays.asList(1L, 2L, 3L);
        SimpleResponse expectedResponse = new SimpleResponse(true, "All applications deleted successfully");
        Mockito.when(applicationService.deleteAll(listId)).thenReturn(expectedResponse);
        SimpleResponse response = applicationApi.deleteAll(listId);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetAllApplication() {
        List<ApplicationResponse> expectedResponse = Arrays.asList(new ApplicationResponse(), new ApplicationResponse());
        Mockito.when(applicationService.getAllApplications()).thenReturn(expectedResponse);
        List<ApplicationResponse> response = applicationApi.getAllApplication();
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testProcessed() {
        ApplicationProcessedRequest processed = new ApplicationProcessedRequest();
        Mockito.when(applicationService.processedById(processed)).thenReturn(true);
        boolean response = applicationApi.processed(processed);
        assertTrue(response);
    }
}