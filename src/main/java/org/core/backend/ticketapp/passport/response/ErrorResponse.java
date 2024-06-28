package org.core.backend.ticketapp.passport.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.core.backend.ticketapp.passport.model.FieldError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @JsonIgnore
    private int httpStatus;
    @NonNull
    private String code;
    @NonNull
    private String message;
    private List<FieldError> errors;
}
