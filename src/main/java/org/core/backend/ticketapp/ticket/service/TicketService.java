package org.core.backend.ticketapp.ticket.service;

import org.core.backend.ticketapp.order.dto.OrderCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketUpdateRequestDTO;
import org.core.backend.ticketapp.ticket.entity.Ticket;

import java.util.UUID;

public interface TicketService {
    Ticket create(final TicketCreateRequestDTO ticketDTO, final OrderCreateRequestDTO orderDTO);

    Ticket getById(final UUID id);

    Ticket update(final UUID id, TicketUpdateRequestDTO requestDTO);

    void delete(final UUID id);
}
