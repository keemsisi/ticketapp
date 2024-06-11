package com.project.core.ticketapp.exceptions;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ExceptionResponse {

    private int status;

    private Date timestamp;

    private String message;

    private String error;

}
