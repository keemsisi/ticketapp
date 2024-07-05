package org.core.backend.ticketapp.ticket.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.passport.dtos.core.UserDto;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
@Validated
public class TicketController {

    private final TicketService ticketService;
    private final CoreUserService userService;
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<List<TicketCreateRequestDTO>> getAllTickets() {
        List<TicketCreateRequestDTO> tickets = ticketService.getAllTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCreateRequestDTO> getTicketById(@PathVariable UUID id) {
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));
        return ResponseEntity.ok(convertToDTO(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketCreateRequestDTO> createTicket(@Valid @RequestBody TicketCreateRequestDTO ticketDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            UserDto userDto = new UserDto();
            userDto.setEmail(ticketDTO.getEmail());
            userDto.setFirstName(ticketDTO.getFirstName());
            userDto.setLastName(ticketDTO.getLstName());
            userDto.setPhone(ticketDTO.getPhoneNumber());

            User user = userService.createUser(userDto, null);
            ticketDTO.setUserId(user.getId());
        } else {
            UUID loggedInUserId = jwtTokenUtil.getUser().getUserId();
            ticketDTO.setUserId(loggedInUserId);
        }

        Ticket createdTicket = ticketService.create(ticketDTO);
        return new ResponseEntity<>(convertToDTO(createdTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketCreateRequestDTO> updateTicket(@PathVariable UUID id, @Valid @RequestBody TicketCreateRequestDTO ticketDTO) {
        Ticket updatedTicket = ticketService.update(id, ticketDTO);
        return ResponseEntity.ok(convertToDTO(updatedTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to convert Ticket entity to TicketDTO
    private TicketCreateRequestDTO convertToDTO(Ticket ticket) {
        TicketCreateRequestDTO ticketDTO = new TicketCreateRequestDTO();
        ticketDTO.setPrice(ticket.getPrice());
        if (ticket.getEventId() != null) {
            ticketDTO.setEventId(ticket.getEventId());
        }
        return ticketDTO;
    }

}
