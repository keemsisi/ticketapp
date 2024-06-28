package org.core.backend.ticketapp.passport.error.exception;

import lombok.Getter;

@Getter
public class AuthorizationParameterNotFoundException extends RuntimeException {

    private String parameter;
    private String parameterType;

    public AuthorizationParameterNotFoundException(String parameter, String parameterType) {
        super(String.format("Missing authorization %s: %s", parameterType, parameter));
        this.parameter = parameter;
        this.parameterType = parameterType;
    }
}
