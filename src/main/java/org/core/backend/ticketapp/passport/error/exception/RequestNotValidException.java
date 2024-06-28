package org.core.backend.ticketapp.passport.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

@Getter
@RequiredArgsConstructor
public class RequestNotValidException extends RuntimeException {

    private final Errors errors;

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("Validation failed for request object '");
        var objectName = this.errors.getObjectName();
        sb.append(objectName.substring(objectName.lastIndexOf('.') + 1))
            .append("', with ").append(this.errors.getErrorCount()).append(" error(s): ");
        for (ObjectError error : this.errors.getAllErrors()) {
            sb.append("[").append(error).append("] ");
        }
        return sb.toString();
    }
}
