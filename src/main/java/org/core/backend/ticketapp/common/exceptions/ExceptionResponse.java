package org.core.backend.ticketapp.common.exceptions;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExceptionResponse {

    private int status;

    private Date timestamp;

    private String message;

    private String error;

}
