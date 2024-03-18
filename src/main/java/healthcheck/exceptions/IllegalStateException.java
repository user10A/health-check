package healthcheck.exceptions;

import lombok.Getter;

@Getter
public class IllegalStateException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public IllegalStateException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public IllegalStateException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }}
