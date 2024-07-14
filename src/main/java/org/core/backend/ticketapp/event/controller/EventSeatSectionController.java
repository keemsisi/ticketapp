package org.core.backend.ticketapp.event.controller;

import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.event.dto.CreateEventSeatSectionDTO;
import org.core.backend.ticketapp.event.dto.EventSeatSectionUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.service.EventSeatSectionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/event-seat-sections")
public record EventSeatSectionController(EventSeatSectionService service) implements ICrudController {

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<List<EventSeatSection>>> create(@RequestBody CreateEventSeatSectionDTO request) {
        final var data = service.create(request);
        return ResponseEntity.ok(new GenericResponse<>("00", "Created successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EventSeatSection>> getById(@PathVariable UUID id) {
        final var data = service.getById(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Fetched successfully", data));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EventSeatSection>> update(@RequestBody @Valid EventSeatSectionUpdateRequestDTO request) {
        final var data = service.update(request);
        return ResponseEntity.ok(new GenericResponse<>("00", "Updated successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Deleted successfully", null));
    }
}
