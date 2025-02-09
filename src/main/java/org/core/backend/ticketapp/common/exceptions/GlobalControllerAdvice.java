package org.core.backend.ticketapp.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;


@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice extends DefaultResponseErrorHandler {

    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<GenericApiResponse<?>> handleInternalServerExceptions(ApplicationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getHttpStatus())
                .body(new GenericApiResponse<>(ex.getCode(), ex.getMessage(), ex.getData()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public GenericApiResponse<?> handleInternalServerExceptions(InvalidDataAccessResourceUsageException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericApiResponse<>("01", "Trying to access a resources which does not exist or with invalid request",
                null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public GenericApiResponse<?> handleInternalServerExceptions(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericApiResponse<>("01", ex.getMessage(), null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    public GenericApiResponse<?> handleInternalServerExceptions(MethodArgumentConversionNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericApiResponse<>("01", "Invalid parameter, check the values and try again", null);
    }

    private ResponseEntity<ExceptionResponse> exception(final Exception exception, final HttpStatus httpStatus) {
        String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        final ExceptionResponse errorResponse = ExceptionResponse.builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .timestamp(new Date())
                .message(message)
                .build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
