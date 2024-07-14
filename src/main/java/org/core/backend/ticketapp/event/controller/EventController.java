package org.core.backend.ticketapp.event.controller;

import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.PagedMapperUtil;
import org.core.backend.ticketapp.common.PagedResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
public record EventController(EventService eventService) implements ICrudController {

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Event>> create(@Valid @RequestBody EventCreateRequestDTO request) {
        final var event = eventService.create(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Created successfully", event),
                HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public ResponseEntity<GenericResponse<Event>> getById(UUID id) {
        final var event = eventService.getById(id);
        return new ResponseEntity<>(new GenericResponse<>("00", "Created successfully", event),
                HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/filter-search")
    public ResponseEntity<GenericResponse<PagedResponse<?>>> filterSearch(final EventFilterRequestDTO filter) {
        return new ResponseEntity<>(new GenericResponse<>("00", "All events",
                PagedMapperUtil.map(eventService.searchEvents(filter))), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Event>> update(@RequestBody EventUpdateRequestDTO request) {
        final var event = eventService.update(request.id(), request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Updated successfully", event),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAll() {
        List<Event> events = eventService.getAll();
        return new ResponseEntity<>(new GenericResponse<>("00", "All events", events),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Event>> delete(@PathVariable UUID id) {
        eventService.delete(id);
        return new ResponseEntity<>(new GenericResponse<>("00", "Deleted successfully", null),
                HttpStatus.OK);
    }
}
