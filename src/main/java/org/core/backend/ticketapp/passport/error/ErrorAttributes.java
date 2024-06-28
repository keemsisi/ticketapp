package org.core.backend.ticketapp.passport.error;

import org.core.backend.ticketapp.common.response.ErrorResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;

public class ErrorAttributes extends DefaultErrorAttributes {

    private final ErrorMapper errorMapper;

    public ErrorAttributes(ErrorMapper errorMapper) {
        this.errorMapper = errorMapper;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable throwable = getError(webRequest);
        if (Objects.nonNull(throwable)) {
            errorAttributes.clear();
            ErrorResponse response = errorMapper.throwableToErrorResponse(throwable);
            webRequest.setAttribute("javax.servlet.error.status_code", response.getHttpStatus(), RequestAttributes.SCOPE_REQUEST);
            errorAttributes.put("code", response.getCode());
            errorAttributes.put("message", response.getMessage());

            if (Objects.nonNull(response.getErrors()) && !response.getErrors().isEmpty()) {
                errorAttributes.put("errors", response.getErrors());
            }
        }
        return errorAttributes;
    }
}
