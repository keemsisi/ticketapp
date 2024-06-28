package org.core.backend.ticketapp.passport.error.exception;

import lombok.Getter;

@Getter
public class AuthorizationStatusNotDeterminedException extends RuntimeException {

    public AuthorizationStatusNotDeterminedException(String message, Throwable cause) {
        super(message, cause);
    }
}
