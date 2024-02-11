package healthcheck.jms;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JmsService {

    private final JmsTemplate jmsTemplate;

    public void sendMessage(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    public String receiveMessage(String destination) {
        return (String) jmsTemplate.receiveAndConvert(destination);
    }
}
