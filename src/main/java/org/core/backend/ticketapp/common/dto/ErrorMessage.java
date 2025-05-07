package org.core.backend.ticketapp.common.dto;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR(50001, "Failed to complete request due to server error"),
    USER_NOT_FOUND(40001, "Oops! User not found exception!"),
    RESOURCE_NOT_FOUND(40002, "Oops! Resource not found!"),
    MEETING_NOT_FOUND(40003, "Oops! Meeting not found!");
    Integer code;
    String message;

    ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
