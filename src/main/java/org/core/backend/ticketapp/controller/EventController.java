package org.core.backend.ticketapp.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.dto.EventRequestDTO;
import org.core.backend.ticketapp.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventRequestDTO>> getAllEvents() {
        List<EventRequestDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRequestDTO> getEventById(@PathVariable("id") Long id) {
        EventRequestDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventRequestDTO> createEvent(@RequestBody EventRequestDTO eventDTO) {
        EventRequestDTO newEvent = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

}
