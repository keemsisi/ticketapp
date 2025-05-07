package org.core.backend.ticketapp.passport.dao;

import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QrCodeDAOService {

    Page<QrCode> getAll(UUID tenantId, UUID userId, FilterTicketRequestDTO prp, Pageable pageable);
}
