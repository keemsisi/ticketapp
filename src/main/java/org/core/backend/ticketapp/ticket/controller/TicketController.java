package org.core.backend.ticketapp.ticket.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.PagedMapperUtil;
import org.core.backend.ticketapp.common.PagedResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketUpdateRequestDTO;
import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/api/v1/tickets")
@RestController
@AllArgsConstructor
public class TicketController implements ICrudController {
    private final TicketService ticketService;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody TicketCreateRequestDTO request) {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var data = ticketService.create(request);
        return ResponseEntity.ok(new GenericResponse<>("00", "Ticket created successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Ticket>> getById(@PathVariable UUID id) {
        final var data = ticketService.getById(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Fetched successfully", data));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Ticket>> update(@RequestBody @Valid TicketUpdateRequestDTO request) {
        final var data = ticketService.update(request);
        return ResponseEntity.ok(new GenericResponse<>("00", "Updated successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        ticketService.delete(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Deleted successfully", null));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<PagedResponse<?>>> getAll(FilterTicketRequestDTO requestDTO, PageRequest pageRequest) {
        final var records = PagedMapperUtil.map(ticketService.getAll(requestDTO, pageRequest));
        return ResponseEntity.ok(new GenericResponse<>("00", "Successfully fetched records", records));
    }
}
