package healthcheck.exceptions;

import lombok.Getter;

@Getter
public class ConstraintsViolationException extends RuntimeException{
    private final String messageCode;
    private final Object[] args;

    public ConstraintsViolationException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public ConstraintsViolationException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }}
