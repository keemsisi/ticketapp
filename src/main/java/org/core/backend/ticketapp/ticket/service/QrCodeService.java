package org.core.backend.ticketapp.ticket.service;


import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeStatsDTO;
import org.core.backend.ticketapp.ticket.dto.ScannedQrCodeResponse;
import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QrCodeService {
    QrCode create(final QrCodeCreateRequestDTO requestDTO);

    QrCode getById(final UUID id);

    void delete(final UUID id);

    Page<QrCode> getAllV2(FilterTicketRequestDTO requestDTO, Pageable pageable);

    ScannedQrCodeResponse scanQr(final String id);

    QrCodeStatsDTO getStats(final UUID eventId);
}
