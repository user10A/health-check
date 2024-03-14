package healthcheck.local;

import healthcheck.dto.SimpleResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
public class MyLocaleResolver implements LocaleResolver,ResolverService {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        if (language == null || language.isEmpty()) {
            return Locale.forLanguageTag("en");
        }
        Locale locale = Locale.forLanguageTag(language);
        if (LanguageConfig.LOCALES.contains(locale)) {
            return locale;
        }
        return Locale.forLanguageTag("en");
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }

    @Override
    public SimpleResponse setLocaleLanguage(Locale locale) {
        Locale locale1 = locale;
        if (locale1 != null) {
            String localeLanguage;
            switch (locale1.getLanguage()) {
                case "en":
                    localeLanguage = "English language";
                    break;
                case "ru":
                    localeLanguage = "Русский язык";
                    break;
                case "es":
                    localeLanguage = "Español language";
                    break;
                default:
                    localeLanguage = "Unknown language";
                    break;
            }
            Locale.setDefault(locale1);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message(localeLanguage)
                    .build();
        } else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("error")
                    .build();
        }
    }
}
