package healthcheck.exceptions;

public class ConstraintsViolationException extends RuntimeException{
    public ConstraintsViolationException(String message){
        super(message);
    }
}
