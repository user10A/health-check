package healthcheck.exceptions;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public AuthenticationException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public AuthenticationException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }}