package org.core.backend.ticketapp.event.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.dto.EventRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.entity.EventSeatSections;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionsRepository;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private JwtTokenUtil jwtTokenUtil;
    private EventSeatSectionsRepository eventSeatSectionsRepository;

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

    @Transactional
    public EventRequestDTO createEvent(EventRequestDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var savedEvent = eventRepository.save(event);
        final var seatSections = new ArrayList<EventSeatSections>();
        eventDTO.getSeatSections().forEach(seatSection -> {
            final var seatSectionsVal = EventSeatSections.builder().eventId(savedEvent.getId())
                    .acquired(0L).capacity(seatSection.getCapacity()).userId(userId)
                    .name(seatSection.getName()).build();
            seatSections.add(seatSectionsVal);
        });
        eventSeatSectionsRepository.saveAll(seatSections);
        return convertToDTO(savedEvent);
    }

    private EventRequestDTO convertToDTO(Event event) {
        return modelMapper.map(event, EventRequestDTO.class);
    }

    private Event convertToEntity(EventRequestDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }
}
