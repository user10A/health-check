package healthcheck.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.context.MessageSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface ValidPhoneNumber {
    String message() default "error.valid_phone_number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}