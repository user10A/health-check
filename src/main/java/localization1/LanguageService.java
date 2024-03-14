//package localization1;
//
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import java.util.Locale;
//
//@Component
//public class LanguageService {
//    @Autowired
//    private MessageSource messageSource;
//
//    private static final String LANGUAGE_SESSION_ATTRIBUTE = "selectedLanguage";
//
//    public void changeLanguage(String language) {
//        Locale locale = new Locale(language);
//        LocaleContextHolder.setLocale(locale);
//    }
//
//    public String getMessage(String key) {
//        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
//    }
//
//
//    public void initializeLanguage() {
////        String language = (String) httpSession.getAttribute(LANGUAGE_SESSION_ATTRIBUTE);
////        if (language != null) {
////            changeLanguage(language);
////        }
//    }
//
//    // Другие методы, которым требуется локализованное сообщение
//    public void someOtherMethod() {
//        // Используйте getMessage() для получения локализованного сообщения
//        String message = getMessage("some.key");
//        // ...
//    }
//}