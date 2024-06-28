package org.core.backend.ticketapp.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.GenericResponse;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;


@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handConstraintValidationException(final ConstraintViolationException exception) {
        return exception(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<GenericResponse<?>> handleInternalServerExceptions(ApplicationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getHttpStatus())
                .body(new GenericResponse<>(ex.getCode(), ex.getMessage(), ex.getData()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public GenericResponse<?> handleInternalServerExceptions(HttpMediaTypeNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericResponse<>("01", ex.getMessage(), null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public GenericResponse<?> handleInternalServerExceptions(InvalidDataAccessResourceUsageException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericResponse<>("01", "Trying to access a resources which does not exist or with invalid request",
                null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public GenericResponse<?> handleInternalServerExceptions(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericResponse<>("01", ex.getMessage(), null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public GenericResponse<?> bindExceptionHandler(BindException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericResponse<>("01", "Oops! Please check all fields are entered correctly!",
                null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    public GenericResponse<?> handleInternalServerExceptions(MethodArgumentConversionNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return new GenericResponse<>("01", "Invalid parameter, check the values and try again", null);
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
