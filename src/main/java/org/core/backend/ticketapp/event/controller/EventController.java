package org.core.backend.ticketapp.event.controller;

import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.PagedMapperUtil;
import org.core.backend.ticketapp.common.PagedResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
public record EventController(EventService eventService) implements ICrudController {
    public ResponseEntity<?> create(EventCreateRequestDTO request) {
        EventCreateRequestDTO event = eventService.create(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Event created successfully", event),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getById(UUID id) {
        return ICrudController.super.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/filter-search")
    public ResponseEntity<GenericResponse<PagedResponse<?>>> filterSearch(final EventFilterRequestDTO filter) {
        return new ResponseEntity<>(new GenericResponse<>("00", "All events",
                PagedMapperUtil.map(eventService.searchEvents(filter))), HttpStatus.OK);
    }

    @Override
    public <T> ResponseEntity<?> update(UUID id, T request) {
        return ICrudController.super.update(id, request);
    }

    @Override
    public ResponseEntity<?> getAll() {
        List<Event> events = eventService.getAll();
        return new ResponseEntity<>(new GenericResponse<>("00", "All events", events),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        return ICrudController.super.delete(id);
    }
}
