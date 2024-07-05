package org.core.backend.ticketapp.event.service.impl;

import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.entity.EventSeatSections;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionsRepository;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private ModelMapper modelMapper;
    private JwtTokenUtil jwtTokenUtil;
    private EventSeatSectionsRepository eventSeatSectionsRepository;

    public List<EventCreateRequestDTO> getAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map((event) -> modelMapper.map(event, EventCreateRequestDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public EventCreateRequestDTO create(EventCreateRequestDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var savedEvent = eventRepository.save(event);
        final var seatSections = new ArrayList<EventSeatSections>();
        eventDTO.getSeatSections().forEach(seatSection -> {
            final var seatSectionsVal = new EventSeatSections(savedEvent.getId(),
                    userId, seatSection.getName(), seatSection.getCapacity(), 0L, ApprovalStatus.APPROVED);
            seatSections.add(seatSectionsVal);
        });
        eventSeatSectionsRepository.saveAll(seatSections);
        return convertToDTO(savedEvent);
    }

    public EventCreateRequestDTO getById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found", id.toString()));
        return convertToDTO(event);
    }

    public Event update(UUID id, EventUpdateRequestDTO eventRequestDTO) {
        Event event = eventRepository.getById(id);
        event.setTitle(eventRequestDTO.title());
        event.setDescription(eventRequestDTO.description());
        event.setEventBanner(eventRequestDTO.eventBanner());
        event.setTicketsAvailable(eventRequestDTO.ticketsAvailable());
        event.setFreeEvent(eventRequestDTO.freeEvent());
        event.setPhysicalEvent(eventRequestDTO.physicalEvent());
        event.setMaxPerUser(eventRequestDTO.maxPerUser());
        event.setLocation(eventRequestDTO.location());
        event.setLocationNumber(eventRequestDTO.locationNumber());
        event.setStreetAddress(eventRequestDTO.streetAddress());
        return eventRepository.save(event);
    }

    public void delete(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found", id.toString()));
        eventRepository.delete(event);
    }
    private EventCreateRequestDTO convertToDTO(Event event) {
        return modelMapper.map(event, EventCreateRequestDTO.class);
    }

    private Event convertToEntity(EventCreateRequestDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }
}
