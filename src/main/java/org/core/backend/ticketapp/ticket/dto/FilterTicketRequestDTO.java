package org.core.backend.ticketapp.ticket.dto;


import java.util.UUID;

public record FilterTicketRequestDTO(
        UUID eventId,
        UUID userId,
        UUID tenantId) {
}
