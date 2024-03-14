package healthcheck.api;

import healthcheck.dto.SimpleResponse;
import healthcheck.local.MyLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/language")
@RequiredArgsConstructor
@Slf4j
public class LanguageApi {
    private final MyLocaleResolver myLocaleResolver;

    @PostMapping()
    public ResponseEntity<Void> resolveLocale(HttpServletRequest request) {
        myLocaleResolver.resolveLocale(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change")
    public SimpleResponse changeLanguage(@RequestParam String language, HttpServletRequest request, HttpServletResponse response) {
        log.info("language: {}", language);
        Locale locale = switch (language) {
            case "en" -> Locale.ENGLISH;
            case "ru" -> new Locale("ru", "RU");
            case "es" -> new Locale("es", "ES");
            default -> Locale.ENGLISH; // Локаль по умолчанию
        };
        myLocaleResolver.setLocale(request, response, locale); // Устанавливаем новую локаль
        return myLocaleResolver.setLocaleLanguage(locale);
    }







//    @Autowired
//    private MessageSource messageSource;
//    @Autowired
//    private MyLocaleResolver myLocaleResolver;
//
//    @PostMapping("/{lang}")
//    public ResponseEntity<Void> changeLanguage(HttpServletRequest httpRequest) {
//        messageSource.getMessage(null,myLocaleResolver.resolveLocale(httpRequest));
//        return ResponseEntity.ok().build();
//    }
//    private  final LanguageService languageService;
//
//    @PostMapping()
//    public ResponseEntity<String> changeLanguage(@RequestParam(name = "language", defaultValue = "en") String language) {
//        languageService.changeLanguage(language);
//        return ResponseEntity.ok("Language changed to " + language);
//    }
}
