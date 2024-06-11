package com.project.core.ticketapp.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{

    private final String code;
    @JsonIgnore
    private final int httpStatus;
    private Object data;

    public ApplicationException(int httpStatus, String code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ApplicationException(int httpStatus, String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ApplicationException(int httpStatus, String code, String message, Object data) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.data = data;
    }
}
