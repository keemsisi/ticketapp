package org.core.backend.ticketapp.ticket.service;


import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketUpdateRequestDTO;
import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface TicketService {
    Ticket create(final TicketCreateRequestDTO requestDTO);

    Ticket getById(final UUID id);

    Ticket update(TicketUpdateRequestDTO requestDTO);

    void delete(final UUID id);

    Page<Ticket> getAll(final FilterTicketRequestDTO requestDTO, final PageRequest pageRequest);
}
