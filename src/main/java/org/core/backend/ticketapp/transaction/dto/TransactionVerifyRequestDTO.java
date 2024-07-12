package org.core.backend.ticketapp.transaction.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionVerifyRequestDTO(
        @NotNull String reference,
        @NotNull String status,
        @NotNull String orderId
) {
}
