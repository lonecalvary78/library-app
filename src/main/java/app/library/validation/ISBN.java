package app.library.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ISBNValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface ISBN {
    String message() default "Invalid ISBN format. Must be a valid 10-digit or 13-digit ISBN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 