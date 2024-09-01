package org.core.backend.ticketapp.ticket.service;


import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.ScannedQrCodeResponse;
import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QrCodeService {
    QrCode create(final QrCodeCreateRequestDTO requestDTO);
    QrCode getById(final UUID id);

    void delete(final UUID id);

    Page<QrCode> getAll(final FilterTicketRequestDTO requestDTO, final Pageable pageRequest);

    ScannedQrCodeResponse scanQr(UUID id);
}
