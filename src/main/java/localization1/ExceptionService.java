//package localization1;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ExceptionService {
//    private final MessageSource messageSource;
//
//    @Autowired
//    public ExceptionService(MessageSource messageSource) {
//        this.messageSource = messageSource;
//    }
//
//    public String getErrorMessage(String key) {
//        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
//    }
//}
