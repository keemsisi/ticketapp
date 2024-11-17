package org.core.backend.ticketapp.ticket.dto;

public record QrCodeStatsDTO(
        Long scanned,
        Long notScanned
) {
}
