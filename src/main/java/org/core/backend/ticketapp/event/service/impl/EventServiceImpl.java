package org.core.backend.ticketapp.event.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.*;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.common.response.EventStatsResponseDTO;
import org.core.backend.ticketapp.common.response.EventTicketStatsDTO;
import org.core.backend.ticketapp.event.dao.EventDao;
import org.core.backend.ticketapp.event.dao.EventResponseDTO;
import org.core.backend.ticketapp.event.dto.AssignCategoryToEventRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventStatRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.entity.EventCategory;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.BankAccountDetailsRepository;
import org.core.backend.ticketapp.event.repository.EventCategoryRepository;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionRepository;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.event.service.VirtualEventService;
import org.core.backend.ticketapp.passport.dtos.NotificationRequestDto;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.service.core.notification.NotificationServiceServiceImpl;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
    private EventRepository eventRepository;
    private EventDao eventDao;
    private ModelMapper modelMapper;
    private JwtTokenUtil jwtTokenUtil;
    private EventSeatSectionRepository eventSeatSectionsRepository;
    private EventCategoryRepository eventCategoryRepository;
    private BankAccountDetailsRepository bankAccountDetailsRepository;
    private VirtualEventService virtualEventService;
    private AppConfigs appConfigs;
    private NotificationServiceServiceImpl notificationServiceImpl;
    private ObjectMapper objectMapper;

    public List<Event> getAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map((event) -> {
            Event eventDTO = modelMapper.map(event, Event.class);
            eventDTO.setSeatSections(event.getSeatSections());
            return eventDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public org.core.backend.ticketapp.common.Page<EventResponseDTO> searchEvents(final EventFilterRequestDTO filterRequest) {
        return eventDao.filterSearch(filterRequest);
    }

    @Override
    @Transactional
    public Event create(@NotNull final EventCreateRequestDTO request) throws Exception {
        final var event = convertToEntity(request);
        final var user = jwtTokenUtil.getUser();
        final var userId = user.getUserId();
        final var tenantId = jwtTokenUtil.getUser().getTenantId();

        Set<EventCategory> existingCategories = new HashSet<>();
        if (Objects.nonNull(request.getCategories()) && !request.getCategories().isEmpty()) {
            final var eventCategories = request.getCategories().stream().map(String::toUpperCase).toList();
            existingCategories = eventCategoryRepository.findAllByName(eventCategories);
            if (existingCategories.size() != eventCategories.size()) {
                throw new ApplicationException(400, "missing_categories", "Some categories does not exist!");
            }
        }

        final var totalCapacity = request.getSeatSections().stream().map(EventSeatSection::getCapacity).mapToInt(Long::intValue).sum();
        final var newCategory = existingCategories.stream().map(EventCategory::getName)
                .map(String::toUpperCase).collect(Collectors.toSet());
        event.setCategories(newCategory);
        event.setTicketsAvailable(totalCapacity);
        event.setId(UUID.randomUUID());
        event.setUserId(userId);
        event.setTenantId(tenantId);
        event.setDateCreated(LocalDateTime.now());
        event.setType(request.getEventType());
        event.setPublic(request.isPublic());

        if (request.getBankAccountDetails() != null) {
            final var bankDetails = modelMapper.map(request.getBankAccountDetails(), BankAccountDetails.class);
            bankDetails.setUserId(userId);
            bankDetails.setId(UUID.randomUUID());
            bankDetails.setTenantId(tenantId);
            bankDetails.setAccountNumberType(request.getBankAccountDetails().getType());
            bankAccountDetailsRepository.save(bankDetails);
        }

        final var seatSections = new ArrayList<EventSeatSection>();
        request.getSeatSections().forEach(seatSection -> {
            final var seatSectionsVal = new EventSeatSection(event.getId(), userId, seatSection.getType(),
                    seatSection.getCapacity(), seatSection.getPrice(), 0L, ApprovalStatus.APPROVED);
            seatSectionsVal.setTenantId(tenantId);
            seatSectionsVal.setDateCreated(LocalDateTime.now());
            seatSections.add(seatSectionsVal);
        });
        eventRepository.saveAndFlush(event);
        eventSeatSectionsRepository.saveAll(seatSections);
        event.setSeatSections(seatSections);
        CompletableFuture.runAsync(() -> {
            try {
                final var notificationRequest = NotificationRequestDto
                        .builder()
                        .actionName(ModuleAction.CREATE_EVENT.getAction())
                        .dateCreated(event.getDateCreated())
                        .approvalStatus(ApprovalStatus.PENDING)
                        .requestedBy(user.getUserId().toString())
                        .tenantId(user.getTenantId().toString())
                        .notificationType(NotificationType.APPROVAL)
                        .description(String.format("This event is created by %s and requires and admin approval", user.getFullName()))
                        .moduleId(appConfigs.eventModuleId.toString())
                        .dateRequested(event.getDateCreated())
                        .newData(objectMapper.writeValueAsString(event)).build();
                notificationServiceImpl.processNotification(notificationRequest, ModuleAction.CREATE_EVENT.getModule());
            } catch (Exception e) {
                log.error(">>> Error Occurred while creating notification for approval", e);
                throw new RuntimeException(e);
            }
        });
        return event;
    }

    public Event getById(UUID id) {
        final var event = eventRepository.findById(id).orElseThrow(() -> new ApplicationException(404, "not_found", "Event not found!"));
        final var seatSections = eventSeatSectionsRepository.getAllByEventId(event.getId());
        event.setSeatSections(seatSections);
        return event;
    }

    @Override
    public List<Event> getAllByIds(final List<UUID> ids) {
        final var userType = jwtTokenUtil.getUser().getUserType();
        if (Objects.isNull(userType)) {
            return eventRepository.findAllById(ids);
        }
        return userType == UserType.BUYER ?
                eventRepository.findAllById(ids)
                : eventRepository.findAllById(ids, jwtTokenUtil.getUser().getUserId());
    }

    public Event update(final EventUpdateRequestDTO request) {
        final var event = eventRepository.findById(request.id()).orElseThrow(this::notFoundException);
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
    public EventTicketStatsDTO getEventTicketStats(final UUID eventSeatSectionId) {
        return eventDao.getEventTicketStats(eventSeatSectionId);
    }

    @Override
    public EventStatsResponseDTO getEventStats(final EventStatRequestDTO request) {
        if (Objects.nonNull(request.getEventId())) {
            validateEventExists(request.getEventId());
        }
        final var user = jwtTokenUtil.getUser();
        if (Objects.nonNull(user.getUserType()) && user.getUserType().isBuyer()) {
            request.setUserId(user.getUserId());
        }
        request.setTenantId(UserUtils.getTenantId(jwtTokenUtil.getUser(), request.getTenantId()));
        return eventDao.getEventsStats(request);
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

    private Event validateEventExists(final UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(this::notFoundException);
    }

    @Override
    public Event save(final Event event) {
        return eventRepository.save(event);
    }
}
