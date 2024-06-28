package org.core.backend.ticketapp.passport.validation;

import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.passport.util.CountryUtil;
import org.core.backend.ticketapp.passport.validation.annotation.CountryCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !StringUtils.isBlank(value) && Objects.nonNull(CountryUtil.getCountry(value));
    }
}
