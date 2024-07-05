package org.core.backend.ticketapp.event.controller;

import org.core.backend.ticketapp.event.service.EventSeatSectionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/event-seat-sections")
public record EventSeatSectionsController(
        EventSeatSectionsService eventSeatSectionsService) implements ICrudController {
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
