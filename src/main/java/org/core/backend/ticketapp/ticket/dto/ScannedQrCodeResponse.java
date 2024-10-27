package org.core.backend.ticketapp.ticket.dto;

import org.core.backend.ticketapp.common.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScannedQrCodeResponse(
        UUID ticketId,
        UUID qrCodeId,
        Status status,
        LocalDateTime dateCreated,
        LocalDateTime dateModified,
        int totalScan
) {
}
