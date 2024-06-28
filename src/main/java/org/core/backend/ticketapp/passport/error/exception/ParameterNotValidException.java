package org.core.backend.ticketapp.passport.error.exception;

import lombok.Getter;

@Getter
public class ParameterNotValidException extends RuntimeException {

    private String parameterName;
    private String rejectedValue;
    private String expectedType;

    public ParameterNotValidException(String parameterName, String rejectedValue, String expectedType) {
        super(String.format("invalid %s [%s] - expected type [%s]", parameterName, rejectedValue, expectedType));
        this.parameterName = parameterName;
        this.expectedType = expectedType;
        this.rejectedValue = rejectedValue;
    }
}
