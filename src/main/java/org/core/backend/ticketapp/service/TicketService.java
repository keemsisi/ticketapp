package org.core.backend.ticketapp.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.exception.ResourceNotFoundException;
import org.core.backend.ticketapp.dto.TicketRequestDTO;
import org.core.backend.ticketapp.entity.Event;
import org.core.backend.ticketapp.entity.Ticket;
import org.core.backend.ticketapp.repository.EventRepository;
import org.core.backend.ticketapp.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(TicketRequestDTO ticketRequestDTO) {
        Event event = eventRepository.findById(ticketRequestDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + ticketRequestDTO.getEventId()));

        Ticket ticket = new Ticket();
        ticket.setPrice(ticketRequestDTO.getPrice());
        ticket.setEvent(event);

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, TicketRequestDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        Event event = eventRepository.findById(ticketDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + ticketDTO.getEventId()));

        ticket.setPrice(ticketDTO.getPrice());
        ticket.setEvent(event);

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        ticketRepository.delete(ticket);
    }
}
