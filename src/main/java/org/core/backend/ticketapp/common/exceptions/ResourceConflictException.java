package org.core.backend.ticketapp.common.exceptions;

import lombok.Getter;

@Getter
public class ResourceConflictException extends RuntimeException{

    private String resource;
    private String resourceId;

    public ResourceConflictException(String resource, String idValue, Throwable cause) {
        super(cause);
        this.resource = resource;
        this.resourceId = String.format("id [%s]", idValue);
    }

    public ResourceConflictException(String resource, String idField, String idValue, Throwable cause) {
        super(cause);
        this.resource = resource;
        this.resourceId = String.format("%s [%s]", idField, idValue);
    }

    @Override
    public String getMessage() {
        return String.format("%s with %s is not in the expected state", resource, resourceId);
    }
}
