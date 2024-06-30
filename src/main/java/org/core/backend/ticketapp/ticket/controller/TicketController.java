package org.core.backend.ticketapp.ticket.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.passport.dtos.core.UserDto;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.ticket.dto.TicketRequestDTO;
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
    public ResponseEntity<List<TicketRequestDTO>> getAllTickets() {
        List<TicketRequestDTO> tickets = ticketService.getAllTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketRequestDTO> getTicketById(@PathVariable UUID id) {
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));
        return ResponseEntity.ok(convertToDTO(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketRequestDTO> createTicket(@Valid @RequestBody TicketRequestDTO ticketDTO) {
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

        Ticket createdTicket = ticketService.createTicket(ticketDTO);
        return new ResponseEntity<>(convertToDTO(createdTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketRequestDTO> updateTicket(@PathVariable UUID id, @Valid @RequestBody TicketRequestDTO ticketDTO) {
        Ticket updatedTicket = ticketService.updateTicket(id, ticketDTO);
        return ResponseEntity.ok(convertToDTO(updatedTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to convert Ticket entity to TicketDTO
    private TicketRequestDTO convertToDTO(Ticket ticket) {
        TicketRequestDTO ticketDTO = new TicketRequestDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setPrice(ticket.getPrice());
        if (ticket.getEventId() != null) {
            ticketDTO.setEventId(ticket.getEventId());
        }
        return ticketDTO;
    }

}
