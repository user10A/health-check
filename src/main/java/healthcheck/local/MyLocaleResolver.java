package healthcheck.local;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@Component
public class MyLocaleResolver implements LocaleResolver{
    @Autowired
    private MessageSource messageSource;
    @Override
    public @NotNull Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        if (language == null || language.isEmpty()) {
            return Locale.forLanguageTag("ru");
        }
        Locale locale = Locale.forLanguageTag(language);
        if (LanguageConfig.LOCALES.contains(locale)) {
            return locale;
        }
        return Locale.forLanguageTag("ru");
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

        throw new UnsupportedOperationException(messageSource.getMessage("error.unsupported_operation_exception",null, LocaleContextHolder.getLocale()));
    }
}
