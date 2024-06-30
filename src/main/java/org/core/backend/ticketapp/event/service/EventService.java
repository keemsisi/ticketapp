package org.core.backend.ticketapp.event.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.dto.EventRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public List<EventRequestDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map((event) -> modelMapper.map(event, EventRequestDTO.class))
                .collect(Collectors.toList());
    }

    public EventRequestDTO getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found", id.toString()));
        return convertToDTO(event);
    }

    public EventRequestDTO createEvent(EventRequestDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        event = eventRepository.save(event);
        return convertToDTO(event);
    }

    private EventRequestDTO convertToDTO(Event event) {
        return modelMapper.map(event, EventRequestDTO.class);
    }

    private Event convertToEntity(EventRequestDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }
}
