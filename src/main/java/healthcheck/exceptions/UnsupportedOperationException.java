package healthcheck.exceptions;

import lombok.Getter;

@Getter
public class UnsupportedOperationException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public UnsupportedOperationException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }

    public UnsupportedOperationException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }}
