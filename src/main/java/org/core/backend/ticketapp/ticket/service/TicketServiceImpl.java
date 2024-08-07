package org.core.backend.ticketapp.ticket.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionRepository;
import org.core.backend.ticketapp.order.dto.OrderCreateRequestDTO;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.repository.OrderRepository;
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
    private final EventSeatSectionRepository eventSeatSectionRepository;
    private final OrderRepository orderRepository;

    @Override
    public Ticket create(TicketCreateRequestDTO ticketRequestDTO, OrderCreateRequestDTO orderCreateRequestDTO) {
        Event event = eventRepository.findById(ticketRequestDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id", ticketRequestDTO.getEventId().toString()));

        if (event.getTicketsAvailable() > 0) {
            event.setTicketsAvailable(event.getTicketsAvailable() - 1);

            EventSeatSection seatSection = null;

            if (!ticketRequestDTO.getSeatSectionId().toString().isEmpty()) {
                seatSection = eventSeatSectionRepository.findById(ticketRequestDTO.getSeatSectionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Event seat section does not exist", ticketRequestDTO.getSeatSectionId().toString()));
                if (seatSection.getCapacity() == 0)
                    throw new IllegalStateException("No available ticket for seat section");

            }

            try {
                // Create new order for payment
                Order ticketOrder = new Order();
                ticketOrder.setEventId(orderCreateRequestDTO.getEventId());
                ticketOrder.setQuantity(orderCreateRequestDTO.getQuantity());
                ticketOrder.setAmount(orderCreateRequestDTO.getTotalAmount());
                Order savedOrder = orderRepository.save(ticketOrder);

                // Initiate payment



                //TODO: Update this code to check if the user exists by email
                // if the user does not exists by email, then create a new user account
                Ticket ticket = new Ticket();
                ticket.setSeatSectionId(ticketRequestDTO.getSeatSectionId());
                ticket.setEventId(ticketRequestDTO.getEventId());
                assert seatSection != null;
                ticket.setPrice(seatSection.getPrice());
                seatSection.setCapacity(seatSection.getCapacity() + 1);

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
            } catch (Exception e) {
                event.setTicketsAvailable(event.getTicketsAvailable() + 1);
                throw new IllegalStateException(e);
            }
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
        eventRepository.findById(ticketDTO.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id", ticketDTO.eventId().toString()));
        ticket.setEventId(ticketDTO.eventId());
        ticket.setSeatSectionId(ticketDTO.seatSectionId());
        return ticketRepository.save(ticket);
    }

    public void delete(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));
        ticketRepository.delete(ticket);
    }
}
