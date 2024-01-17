package healthcheck.api;
import healthcheck.dto.Application.ApplicationRequest;
import healthcheck.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationApi {

    private final ApplicationService applicationService;

    @PostMapping
    public String createApplication(@RequestBody ApplicationRequest applicationRequest){
        applicationService.createApplication(applicationRequest);
        return "Удачно отправлена заявка!";
    }
}