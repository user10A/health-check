package healthcheck.exceptions.handler;

import healthcheck.exceptions.*;
import healthcheck.exceptions.IllegalStateException;
import healthcheck.exceptions.UnsupportedOperationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
@Slf4j
public class AppExceptionHandler {

    @Autowired
    private MessageSource messageSource;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> errors = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ExceptionResponse
                .builder()
                .message(messageSource.getMessage(errors.toString(),null,LocaleContextHolder.getLocale()))
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(e.getClass().getSimpleName())
                .build();
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ExceptionResponse handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder errorMessage = new StringBuilder(messageSource.getMessage("validation.error", null, LocaleContextHolder.getLocale()) + ": ");
        e.getConstraintViolations().forEach(violation -> errorMessage.append(messageSource.getMessage(violation.getMessage(), null, LocaleContextHolder.getLocale())).append("; "));
        return ExceptionResponse.builder()
                .message(errorMessage.toString())
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(e.getClass().getSimpleName())
                .build();
    }
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyExistsException(AlreadyExistsException e){
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException e){
        log.info("NotFoundException: --------------------" + e.getMessage());
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse badCredentialException(BadCredentialsException e){
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAuthenticationException(AuthenticationException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .build();
    }
    @ExceptionHandler(DataUpdateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleDataUpdateException(DataUpdateException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .build();
    }
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse InvalidPasswordException(InvalidPasswordException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .build();
    }
    @ExceptionHandler(S3Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundFileException(NotFoundException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .build();
    }
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
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
    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse HandleConflictException(UnsupportedOperationException e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .build();
    }
}
