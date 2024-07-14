package org.core.backend.ticketapp.passport.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import io.github.thecarisma.InvalidEntryException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.FieldError;
import org.core.backend.ticketapp.common.response.ErrorResponse;
import org.core.backend.ticketapp.passport.error.exception.*;
import org.core.backend.ticketapp.passport.util.EnumUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.internet.AddressException;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class ErrorMapper {

    public static final String BAD_REQUEST_CODE = "bad_request";
    public static final String BAD_REQUEST_MESSAGE = "Request is not valid. Possible syntax or data type mismatch errors";
    public static final String REQUEST_NOT_AVAILABLE_CODE = "request_not_available";
    public static final String SERVER_ERROR_CODE = "server_error";
    public static final String LOG_KEY_MESSAGE_TEMPLATE = "%s. Try again or contact support with this log key: %s";

    public ErrorResponse throwableToErrorResponse(Throwable throwable) {
        log.error("{0}", throwable);
        if (throwable instanceof RequestNotValidException) {
            return fromRequestNotValidException((RequestNotValidException) throwable);
        }

        if (throwable instanceof HttpMessageConversionException) {
            return fromHttpMessageConversionException((HttpMessageConversionException) throwable);
        }

        if (throwable instanceof IllegalArgumentException) {
            return fromIllegalArgumentException((IllegalArgumentException) throwable);
        }

        if (throwable instanceof AddressException) {
            return new ErrorResponse(400, "invalid_email_address", throwable.getMessage());
        }

        if (
                throwable instanceof InvalidEntryException ||
                        throwable instanceof MultipartException ||
                        throwable instanceof ParameterNotValidException || throwable instanceof UnexpectedTypeException
                        || throwable instanceof InvalidDataAccessApiUsageException || throwable instanceof InvalidRequestException
        ) {
            return new ErrorResponse(400, BAD_REQUEST_CODE, throwable.getMessage());
        }

        if (
                throwable instanceof MethodArgumentNotValidException
                        || throwable instanceof BindException
        ) {
            return fromRequestNotValidException((BindException) throwable);
        }

        if (throwable instanceof DuplicateKeyException) {
            return fromDuplicateKeyException((DuplicateKeyException) throwable);
        }

        if (throwable instanceof DataIntegrityViolationException) {
            return fromConstraintViolationException((DataIntegrityViolationException) throwable);
        }

        if (throwable instanceof ConstraintViolationException) {
            return fromConstraintViolationException((ConstraintViolationException) throwable);
        }


        if (throwable instanceof AuthorizationParameterNotFoundException) {
            return new ErrorResponse(401, "unauthorized", throwable.getMessage());
        }

        if (throwable instanceof IllegalArgumentException) {
            return new ErrorResponse(400, "400", throwable.getMessage());
        }

        if (throwable instanceof AuthorizationStatusNotDeterminedException) {
            String logKey = log(throwable);
            return new ErrorResponse(401, "authorization_status_not_determined",
                    String.format(LOG_KEY_MESSAGE_TEMPLATE, "User authorization could not be determined", logKey));
        }

        if (throwable instanceof NoHandlerFoundException e) {
            return new ErrorResponse(404, "path_not_found",
                    String.format("Request path '%s %s' not found", e.getHttpMethod(), e.getRequestURL()));
        }

        if (throwable instanceof ResourceNotFoundException) {
            return new ErrorResponse(404, "resource_not_found", throwable.getMessage());
        }

        if (throwable instanceof HttpRequestMethodNotSupportedException e) {
            return new ErrorResponse(405, "method_not_allowed",
                    String.format("Request method '%s' not supported. Supported: [%s]",
                            e.getMethod(), StringUtils.join(e.getSupportedMethods(), ", ")));
        }

        if (throwable instanceof ResourceConflictException) {
            String logKey = log(throwable);
            return new ErrorResponse(409, "resource_conflict_exception",
                    String.format(LOG_KEY_MESSAGE_TEMPLATE, throwable.getMessage(), logKey));
        }

        if (throwable instanceof IOException ||
                throwable instanceof ResourceAccessException) {
            return new ErrorResponse(500, "connection_error",
                    "Remote entity cannot be reached. Connection reset or broken");
        }

        if (throwable instanceof ApplicationException e) {
            if (e.getCause() == null) {
                return new ErrorResponse(e.getHttpStatus(), e.getCode(), e.getMessage());
            } else {
                String logKey = log(e.getCause());
                return new ErrorResponse(e.getHttpStatus(), e.getCode(),
                        String.format(LOG_KEY_MESSAGE_TEMPLATE, e.getMessage(), logKey));
            }
        }

        if (throwable instanceof RuntimeException e) {
            log.error(">>> Error : ", e);
            String message = BAD_REQUEST_MESSAGE;
            if (e.getMessage().contains("ix_tbl_users_col_email_uq"))
                message = "Oops! One or more users have an existing account email address(es)";
            if (e.getMessage().contains("ix_tbl_event_seat_secs_type_event_id_user_id_uq")) {
                message = "Oops! Duplicate seat section in request!";
            }
            return new ErrorResponse(400, "request_failed",
                    message);
        }

        String logKey = log(throwable);

        return new ErrorResponse(500, "request_failed",
                String.format(LOG_KEY_MESSAGE_TEMPLATE, "Request failed", logKey));
    }

    private ErrorResponse fromRequestNotValidException(RequestNotValidException e) {
        List<FieldError> errors = e.getErrors().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponse(400, BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE, errors);
    }

    private ErrorResponse fromRequestNotValidException(BindException e) {
        List<FieldError> errors = e.getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponse(400, BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE, errors);
    }

    private ErrorResponse fromHttpMessageConversionException(HttpMessageConversionException e) {
        if (e.getCause() instanceof JsonMappingException) {
            return fromJsonMappingException((JsonMappingException) e.getCause());
        }
        return new ErrorResponse(400, BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE);
    }

    private ErrorResponse fromIllegalArgumentException(IllegalArgumentException e) {
        if (e.getCause() instanceof JsonMappingException) {
            return fromJsonMappingException((JsonMappingException) e.getCause());
        }
        return new ErrorResponse(400, BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE);
    }

    private ErrorResponse fromJsonMappingException(JsonMappingException e) {
        List<JsonMappingException.Reference> references = e.getPath();
        references = references.stream().filter(x -> x.getFieldName() != null).collect(Collectors.toList());
        if (references.size() < 1) {
            return new ErrorResponse(400, "400", "Invalid request format.");
        }
        StringBuilder path = new StringBuilder(references.get(0).getFieldName());
        references.subList(1, references.size()).forEach(reference -> {
            if (reference.getIndex() == -1) {
                // object or map
                path.append(".").append(reference.getFieldName());
            } else {
                // collection
                path.append("[").append(reference.getIndex()).append("]");
            }
        });
        String field = path.toString();
        Class type = null;
        if (e instanceof MismatchedInputException) {
            type = ((MismatchedInputException) e).getTargetType();
        } else if (e instanceof InvalidDefinitionException) {
            type = ((InvalidDefinitionException) e).getType().getRawClass();
        } else if (e instanceof ValueInstantiationException) {
            type = ((ValueInstantiationException) e).getType().getRawClass();
        }
        String message = "invalid";
        if (Objects.nonNull(type)) {
            if (type.isEnum()) {
                message = String.format("invalid - expected [%s]",
                        StringUtils.join(EnumUtil.getEnumConstants(type), ", "));
            } else if (type.isAssignableFrom(LocalDate.class)) {
                message = "invalid - expected format [yyyy-MM-dd]";
            }
        }
        return new ErrorResponse(400, BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE, List.of(new FieldError(field, message)));
    }

    private ErrorResponse fromDuplicateKeyException(DuplicateKeyException e) {

        String message = e.getMostSpecificCause().getMessage();
        String tableName = StringUtils.substringBetween(message, "tbl_", "_col");
        String columns = StringUtils.substringBetween(message, "_col_", "_uq");

        String resource = StringUtils.isNotBlank(tableName) ? tableName : "resource";
        String properties = StringUtils.isNotBlank(columns) ?
                columns.replaceAll("__", ", ").replace("_", " ") : "properties";

        return new ErrorResponse(409, String.format("%s_already_exists", resource),
                String.format("%s with matching details already exists", resource.replace("_", " "), properties));
    }

    private ErrorResponse fromDuplicateKeyException(DataIntegrityViolationException e) {

        String message = e.getMostSpecificCause().getMessage();
        String tableName = StringUtils.substringBetween(message, "tbl_", "_col");
        String columns = StringUtils.substringBetween(message, "_col_", "_uq");

        String resource = StringUtils.isNotBlank(tableName) ? tableName : "resource";
        String properties = StringUtils.isNotBlank(columns) ?
                columns.replaceAll("__", ", ").replace("_", " ") : "properties";

        return new ErrorResponse(409, String.format("%s_already_exists", resource),
                String.format("%s with matching details already exists", resource.replace("_", " "), properties));
    }

    private ErrorResponse fromConstraintViolationException(DataIntegrityViolationException e) {

        var message = e.getMostSpecificCause().getMessage();
        String tableName = StringUtils.substringBetween(message, "tbl_", "_col_");
        if (Objects.nonNull(StringUtils.substringBetween(message, "_col_", "_uq"))) {
            return fromDuplicateKeyException(e);
        }
        String resource = StringUtils.isNotBlank(tableName) ? tableName : "resource";
        var _message = String.format("%s with the given id does not exist", resource);
        if (message.contains("update or delete on table")) {
            _message = String.format("The resource can not be deleted has it is still been used by other resources.", resource);
        }
        return new ErrorResponse(400, "400", _message);
    }

    private ErrorResponse fromConstraintViolationException(ConstraintViolationException e) {

        var message = e.getMessage();
        String tableName = StringUtils.substringBetween(message, "propertyPath=", "Id,");
        String resource = StringUtils.isNotBlank(tableName) ? tableName : "resource";
        var _message = String.format("%s with the given id does not exist", resource);
        if (message.contains("update or delete on table")) {
            _message = String.format("This operation is not allowed on %s with the given id.", resource);
        } else if (message.contains("duplicate key value violates unique constraint")) {
            _message = "Oops! Contains duplicate resource!";
        }
        return new ErrorResponse(400, "400", _message);
    }

    private String log(Throwable throwable) {
        String logKey = String.format("ERR-%s",
                RandomStringUtils.randomAlphanumeric(6).toUpperCase(Locale.ROOT));
        log.error(logKey, throwable);
        return logKey;
    }
}
