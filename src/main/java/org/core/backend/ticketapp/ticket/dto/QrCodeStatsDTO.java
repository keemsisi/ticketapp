package org.core.backend.ticketapp.ticket.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record QrCodeStatsDTO(
        Long scanned,
        Long notScanned,
        UUID eventId,
        String title,
        LocalDateTime eventDate,
        String bannerUrl
) {
}
