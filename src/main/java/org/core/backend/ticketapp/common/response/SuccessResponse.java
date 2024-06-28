package org.core.backend.ticketapp.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {
    @JsonProperty("responseCode")
    private String code;
    @JsonProperty("responseMessage")
    private String message;
}
