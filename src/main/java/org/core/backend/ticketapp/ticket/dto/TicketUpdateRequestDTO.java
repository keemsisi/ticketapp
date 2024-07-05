package org.core.backend.ticketapp.ticket.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TicketUpdateRequestDTO(
        @NotNull double price,
        @NotNull String seatSection,
        @NotNull UUID eventId,
        @NotNull UUID userId
        ) {
}
