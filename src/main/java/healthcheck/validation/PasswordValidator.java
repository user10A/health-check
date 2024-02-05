package healthcheck.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null &&
                password.length() >= 8 &&
                password.length() <= 20 &&
                password.matches(".*[A-Z].*") && // хотя бы одна заглавная буква
                password.matches(".*[a-z].*") && // хотя бы одна строчная буква
                password.matches(".*\\d.*") && // хотя бы одна цифра
                password.matches(".*[!@#$%^&*()].*"); // хотя бы один специальный символ
    }
}
