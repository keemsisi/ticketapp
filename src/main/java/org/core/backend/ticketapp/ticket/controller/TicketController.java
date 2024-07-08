package org.core.backend.ticketapp.ticket.controller;

import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public record TicketController (TicketService ticketService) implements ICrudController {
    @Override
    public <T> ResponseEntity<?> create(T request) {
        return ICrudController.super.create(request);
    }

    @Override
    public ResponseEntity<?> getById(UUID id) {
        return ICrudController.super.getById(id);
    }

    @Override
    public <T> ResponseEntity<?> update(T request) {
        return ICrudController.super.update(request);
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        return ICrudController.super.delete(id);
    }
}
