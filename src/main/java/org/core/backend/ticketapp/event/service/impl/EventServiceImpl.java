package org.core.backend.ticketapp.event.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.dao.EventDao;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionRepository;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private EventDao eventDao;
    private ModelMapper modelMapper;
    private JwtTokenUtil jwtTokenUtil;
    private EventSeatSectionRepository eventSeatSectionsRepository;

    public List<Event> getAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map((event) -> {
                    Event eventDTO = modelMapper.map(event, Event.class);
                    eventDTO.setSeatSections(event.getSeatSections());
                    return eventDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<Event> searchEvents(final EventFilterRequestDTO filterRequest) {
        return eventDao.filterSearch(filterRequest);
    }

    @Override
    @Transactional
    public Event create(EventCreateRequestDTO eventDTO) {
        final var event = convertToEntity(eventDTO);
        final var userId = jwtTokenUtil.getUser().getUserId();
        event.setUserId(userId);
        final var savedEvent = eventRepository.save(event);
        final var seatSections = new ArrayList<EventSeatSection>();
        eventDTO.getSeatSections().forEach(seatSection -> {
            final var seatSectionsVal = new EventSeatSection(savedEvent.getId(),
                    userId, seatSection.getType(), seatSection.getCapacity(), seatSection.getPrice(), 0L, ApprovalStatus.APPROVED);
            seatSections.add(seatSectionsVal);
        });
        eventSeatSectionsRepository.saveAll(seatSections);
        event.setSeatSections(seatSections);
        return savedEvent;
    }

    public Event getById(UUID id) {
        final var event = eventRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(404, "not_found", "Event not found!"));
        UserUtils.isResourceOwner(event.getUserId());
        return event;
    }

    public Event update(final UUID id, final EventUpdateRequestDTO eventRequestDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found", id.toString()));
        UserUtils.isResourceOwner(event.getUserId());
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
        event.setEventCategory(eventRequestDTO.eventCategory());
        //TODO: update event section too here
        eventRepository.save(event);
        return event;
    }

    @Override
    public Page<Event> getByEventTicketType(final EventTicketType eventTicketType, final Pageable pageable) {
        return eventRepository.getByEventTicketType(eventTicketType.name(), pageable);
    }

    public void delete(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found", id.toString()));
        UserUtils.isResourceOwner(event.getUserId());
        event.setDeleted(true);
        eventRepository.save(event);
    }

    private EventCreateRequestDTO convertToDTO(Event event) {
        return modelMapper.map(event, EventCreateRequestDTO.class);
    }

    private Event convertToEntity(EventCreateRequestDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }
}
