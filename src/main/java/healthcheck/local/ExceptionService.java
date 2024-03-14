package healthcheck.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionService {

    private final MessageService messageService;

    @Autowired
    public ExceptionService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getErrorMessage(String code) {
        return messageService.getMessage(code);
    }
}

