package org.core.backend.ticketapp.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.exception.ResourceNotFoundException;
import org.core.backend.ticketapp.dto.TicketRequestDTO;
import org.core.backend.ticketapp.entity.Ticket;
import org.core.backend.ticketapp.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketRequestDTO>> getAllTickets() {
        List<TicketRequestDTO> tickets = ticketService.getAllTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketRequestDTO> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return ResponseEntity.ok(convertToDTO(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketRequestDTO> createTicket(@Valid @RequestBody TicketRequestDTO ticketDTO) {
        Ticket createdTicket = ticketService.createTicket(ticketDTO);
        return new ResponseEntity<>(convertToDTO(createdTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketRequestDTO> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketRequestDTO ticketDTO) {
        Ticket updatedTicket = ticketService.updateTicket(id, ticketDTO);
        return ResponseEntity.ok(convertToDTO(updatedTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to convert Ticket entity to TicketDTO
    private TicketRequestDTO convertToDTO(Ticket ticket) {
        TicketRequestDTO ticketDTO = new TicketRequestDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setPrice(ticket.getPrice());
        if (ticket.getEvent() != null) {
            ticketDTO.setEventId(ticket.getEvent().getId());
        }
        return ticketDTO;
    }

}
