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
import org.core.backend.ticketapp.event.entity.EventCategory;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventCategoryRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private EventDao eventDao;
    private ModelMapper modelMapper;
    private JwtTokenUtil jwtTokenUtil;
    private EventSeatSectionRepository eventSeatSectionsRepository;
    private EventCategoryRepository eventCategoryRepository;

    public List<Event> getAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map((event) -> {
            Event eventDTO = modelMapper.map(event, Event.class);
            eventDTO.setSeatSections(event.getSeatSections());
            return eventDTO;
        }).collect(Collectors.toList());
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

        eventDTO.getSubCategories().add(event.getEventCategory());
        final var eventCategories = eventDTO.getSubCategories().stream().map(String::toUpperCase).toList();
        final var existingCategories = eventCategoryRepository.findAllByName(eventDTO.getSubCategories().stream().toList());
        if (existingCategories.size() != eventCategories.size()) {
            throw new ApplicationException(400, "missing_categories", "Some categories does not exist!");
        }
        final var categoriesNames = existingCategories.stream().map(EventCategory::getName).toList();
        event.setSubCategories(categoriesNames);
        event.setEventCategory(eventDTO.getEventCategory());
        event.setId(UUID.randomUUID());
        event.setUserId(userId);

        final var seatSections = new ArrayList<EventSeatSection>();
        eventDTO.getSeatSections().forEach(seatSection -> {
            final var seatSectionsVal = new EventSeatSection(event.getId(), userId,
                    seatSection.getType(), seatSection.getCapacity(), seatSection.getPrice(),
                    0L, ApprovalStatus.APPROVED);
            seatSections.add(seatSectionsVal);
        });
        eventSeatSectionsRepository.saveAll(seatSections);
        event.setSeatSections(seatSections);
        return eventRepository.save(event);
    }

    public Event getById(UUID id) {
        final var event = eventRepository.findById(id).orElseThrow(() -> new ApplicationException(404, "not_found", "Event not found!"));
        UserUtils.isResourceOwner(event.getUserId());
        return event;
    }

    public Event update(final EventUpdateRequestDTO request) {
        Event event = eventRepository.findById(request.id()).orElseThrow(() -> new ResourceNotFoundException("Event not found", id.toString()));
        UserUtils.isResourceOwner(event.getUserId());
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventBanner(request.eventBanner());
        event.setTicketsAvailable(request.ticketsAvailable());
        event.setFreeEvent(request.freeEvent());
        event.setPhysicalEvent(request.physicalEvent());
        event.setMaxPerUser(request.maxPerUser());
        event.setLocation(request.location());
        event.setLocationNumber(request.locationNumber());
        event.setStreetAddress(request.streetAddress());
//        event.setEventCategory(request.eventCategory()); TODO: update event category
        event.setDateModified(LocalDateTime.now());
        //TODO: update event section too here
        return eventRepository.save(event);
    }

    @Override
    public Page<Event> getByEventTicketType(final EventTicketType eventTicketType, final Pageable pageable) {
        return eventRepository.getByEventTicketType(eventTicketType.name(), pageable);
    }

    public void delete(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ApplicationException(404, "not_found", "Resource not found!"));
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
