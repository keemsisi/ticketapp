package org.core.backend.ticketapp.event.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.event.dto.EventRequestDTO;
import org.core.backend.ticketapp.event.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventRequestDTO>> getAllEvents() {
        List<EventRequestDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRequestDTO> getEventById(@PathVariable("id") UUID id) {
        EventRequestDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventRequestDTO> createEvent(@Valid @RequestBody EventRequestDTO eventDTO) {
        EventRequestDTO newEvent = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

}
