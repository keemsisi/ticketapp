package org.core.backend.ticketapp.common.exceptions;

public final class ApplicationExceptionUtils {

    public static void badRequest() {
        throw new ApplicationException(404, "not_found", "Oops! Bad request!");
    }

    public static ApplicationException notFound() {
        return new ApplicationException(404, "not_found", "Resource not found!");
    }
}
