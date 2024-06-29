<<<<<<< HEAD
package org.core.backend.ticketapp.common.exceptions;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    private String resource;
    private String resourceId;

    public ResourceNotFoundException(String resource, String idValue) {
        super();
        this.resource = resource;
        this.resourceId = String.format("id [%s]", idValue);
    }

    public ResourceNotFoundException(String resource, String idField, String idValue) {
        super();
        this.resource = resource;
        this.resourceId = String.format("%s [%s]", idField, idValue);
    }

    public ResourceNotFoundException(String resource, Map<String, Object> parameters) {
        super();
        this.resource = resource;
        List<String> params = parameters.entrySet().stream()
                .map(m -> String.format("%s [%s]", m.getKey(), m.getValue()))
                .collect(Collectors.toList());
        this.resourceId = StringUtils.join(params, ", ");
    }

    @Override
    public String getMessage() {
        return String.format("%s with %s not found", resource, resourceId);
    }

=======
package org.core.backend.ticketapp.common.enums.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
>>>>>>> feature/events-api
}
