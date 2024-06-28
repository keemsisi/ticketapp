package org.core.backend.ticketapp.passport.validation.annotation;

import org.core.backend.ticketapp.passport.validation.CountryCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = CountryCodeValidator.class)
@Documented
public @interface CountryCode {

    String message() default "must be iso alpha-3 country code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
