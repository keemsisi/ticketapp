package org.core.backend.ticketapp.ticket.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.passport.dtos.core.UserDto;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketUpdateRequestDTO;
import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.core.backend.ticketapp.ticket.repository.TicketRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final CoreUserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Ticket create(TicketCreateRequestDTO ticketRequestDTO) {
        Event event = eventRepository.findById(ticketRequestDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id", ticketRequestDTO.getEventId().toString()));

        if (event.getTicketsAvailable() > 0) {
            event.setTicketsAvailable(event.getTicketsAvailable() - 1);

            Ticket ticket = new Ticket();
            ticket.setPrice(ticketRequestDTO.getPrice());
            ticket.setSeatSection(ticketRequestDTO.getSeatSection());
            ticket.setEventId(ticketRequestDTO.getEventId());

            // Check if user exists
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                UserDto userDto = new UserDto();
                userDto.setEmail(ticketRequestDTO.getEmail());
                userDto.setFirstName(ticketRequestDTO.getFirstName());
                userDto.setLastName(ticketRequestDTO.getLastName());
                userDto.setPhone(ticketRequestDTO.getPhoneNumber());

                User user = userService.createUser(userDto, null);
                ticket.setUserId(user.getId());
            } else {
                UUID loggedInUserId = jwtTokenUtil.getUser().getUserId();
                ticket.setUserId(loggedInUserId);
            }

            return ticketRepository.save(ticket);
        }

        throw new IllegalStateException("Ticket not available for event");
    }

    @Override
    public Ticket getById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket does not exist", id.toString()));

        return ticket;
    }

    @Override
    public Ticket update(UUID id, TicketUpdateRequestDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));

        Event event = eventRepository.findById(ticketDTO.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id", ticketDTO.eventId().toString()));

        ticket.setPrice(ticketDTO.price());
        ticket.setEventId(ticketDTO.eventId());

        return ticketRepository.save(ticket);
    }

    public void delete(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));
        ticketRepository.delete(ticket);
    }
}
