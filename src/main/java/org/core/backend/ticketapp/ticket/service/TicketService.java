package org.core.backend.ticketapp.ticket.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.ticket.dto.TicketRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.ticket.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Ticket> getTicketById(UUID id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(TicketRequestDTO ticketRequestDTO) {
        Event event = eventRepository.findById(ticketRequestDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id", ticketRequestDTO.getEventId().toString()));

        Ticket ticket = new Ticket();
        ticket.setPrice(ticketRequestDTO.getPrice());
        ticket.setEventId(ticketRequestDTO.getEventId());

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(UUID id, TicketRequestDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));

        Event event = eventRepository.findById(ticketDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id", ticketDTO.getEventId().toString()));

        ticket.setPrice(ticketDTO.getPrice());
        ticket.setEventId(ticketDTO.getEventId());

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));
        ticketRepository.delete(ticket);
    }
}
