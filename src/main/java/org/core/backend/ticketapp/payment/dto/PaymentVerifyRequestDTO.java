package org.core.backend.ticketapp.payment.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentVerifyRequestDTO(
        @NotNull String reference,
        @NotNull String status,
        @NotNull String orderId
) {
}
