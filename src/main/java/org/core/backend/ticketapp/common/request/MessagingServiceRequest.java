package org.core.backend.ticketapp.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagingServiceRequest {
    @NotNull(message = "to should not be null")
    @NotEmpty(message = "to should not be empty")
    private String to;
    @NotNull(message = "text should not be null")
    @NotEmpty(message = "text should not be empty")
    private String text;
    private String from;
    private String tenantId;
}
