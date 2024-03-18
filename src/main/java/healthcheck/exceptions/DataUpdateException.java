package healthcheck.exceptions;

import lombok.Getter;

@Getter
public class DataUpdateException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public DataUpdateException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public DataUpdateException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }
}