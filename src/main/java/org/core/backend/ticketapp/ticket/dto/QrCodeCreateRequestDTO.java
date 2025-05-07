package org.core.backend.ticketapp.ticket.dto;

import java.util.UUID;

public record QrCodeCreateRequestDTO(
        UUID ticketId) {
}
