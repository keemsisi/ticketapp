package org.core.backend.ticketapp.passport.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Collection;

public class ValidationUtil {

    public static <T> void invokeNestedValidator(Validator validator, T entity, Errors errors, String subPath, Object... validationHints) {
        try {
            errors.pushNestedPath(subPath);
            ValidationUtils.invokeValidator(validator, entity, errors, validationHints);
        } finally {
            errors.popNestedPath();
        }
    }

    public static <T> void invokeNestedValidatorOnCollection(Validator validator, Collection<T> entities, Errors errors, String subPathRoot) {
        int index = 0;
        for (T entity : entities) {
            invokeNestedValidator(validator, entity, errors, subPathRoot + "[" + index + "]");
            index++;
        }
    }

    private ValidationUtil() {}
}
