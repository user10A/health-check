package healthcheck.exceptions.handler;

import healthcheck.exceptions.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(@NotNull MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<String> errorMessages = result.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errorMessages);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse badCredentialsException(BadCredentialsException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyExistsException(AlreadyExistsException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAuthenticationException(AuthenticationException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(DataUpdateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleDataUpdateException(DataUpdateException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse InvalidPasswordException(InvalidPasswordException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(S3Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundFileException(NotFoundException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(IOException.class) // отправить класс
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse HandleConflictException(HttpClientErrorException.Conflict e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

}
