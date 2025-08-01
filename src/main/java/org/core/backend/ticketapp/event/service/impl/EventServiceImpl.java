package org.core.backend.ticketapp.event.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.dao.EventDao;
import org.core.backend.ticketapp.event.dao.EventResponseDTO;
import org.core.backend.ticketapp.event.dto.AssignCategoryToEventRequestDTO;
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

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public Page<EventResponseDTO> searchEvents(final EventFilterRequestDTO filterRequest) {
        return eventDao.filterSearch(filterRequest);
    }

    @Override
    @Transactional
    public Event create(@NotNull final EventCreateRequestDTO eventDTO) {
        final var event = convertToEntity(eventDTO);
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var tenantId = jwtTokenUtil.getUser().getTenantId();

        Set<EventCategory> existingCategories = new HashSet<>();
        if (!eventDTO.getCategories().isEmpty()) {
            final var eventCategories = eventDTO.getCategories().stream().map(String::toUpperCase).toList();
            existingCategories = eventCategoryRepository.findAllByName(eventCategories);
            if (existingCategories.size() != eventCategories.size()) {
                throw new ApplicationException(400, "missing_categories", "Some categories does not exist!");
            }
        }
        final var newCategory = existingCategories.stream().map(EventCategory::getName)
                .map(String::toUpperCase).collect(Collectors.toSet());
        event.setCategories(newCategory);
        event.setId(UUID.randomUUID());
        event.setUserId(userId);
        event.setTenantId(tenantId);

        final var seatSections = new ArrayList<EventSeatSection>();
        eventDTO.getSeatSections().forEach(seatSection -> {
            final var seatSectionsVal = new EventSeatSection(event.getId(), userId, seatSection.getType(),
                    seatSection.getCapacity(), seatSection.getPrice(), 0L, ApprovalStatus.APPROVED);
            seatSectionsVal.setTenantId(tenantId);
            seatSections.add(seatSectionsVal);
        });
        eventRepository.saveAndFlush(event);
        eventSeatSectionsRepository.saveAll(seatSections);
        event.setSeatSections(seatSections);
        return event;
    }

    public Event getById(UUID id) {
        final var event = eventRepository.findById(id).orElseThrow(() -> new ApplicationException(404, "not_found", "Event not found!"));
        UserUtils.canAccessResource(event.getUserId());
        return event;
    }

    public Event update(final EventUpdateRequestDTO request) {
        final var event = eventRepository.findById(request.id()).orElseThrow(this::notFoundException);
        UserUtils.canAccessResource(event.getUserId());
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
        event.setDateModified(LocalDateTime.now());
        event.setCategories(request.categories());
        return eventRepository.save(event);
    }

    @Override
    public Event assignCategory(@NotNull final AssignCategoryToEventRequestDTO request) {
        final var eventCategory = eventCategoryRepository.getAllByIds(request.categoryIds());
        if (eventCategory.isEmpty()) throw notFoundException();
        final var event = eventRepository.findById(request.eventId()).orElseThrow(this::notFoundException);
        final var oldEventCategories = new ArrayList<>(event.getCategories());
        oldEventCategories.addAll(eventCategory.stream().map(EventCategory::getName).collect(Collectors.toSet()));
        final var newCategories = new TreeSet<>(oldEventCategories);
        event.setCategories(newCategories);
        return eventRepository.save(event);
    }

    @Override
    public Page<Event> getByEventTicketType(final EventTicketType eventTicketType, final Pageable pageable) {
        return eventRepository.getByEventTicketType(eventTicketType.name(), pageable);
    }

    public void delete(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ApplicationException(404, "not_found", "Resource not found!"));
        UserUtils.canAccessResource(event.getUserId());
        event.setDeleted(true);
        eventRepository.save(event);
    }

    private EventCreateRequestDTO convertToDTO(Event event) {
        return modelMapper.map(event, EventCreateRequestDTO.class);
    }

    private Event convertToEntity(EventCreateRequestDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }

    private ApplicationException notFoundException() {
        return new ApplicationException(404, "not_found", "Resource not found!");
    }
}
