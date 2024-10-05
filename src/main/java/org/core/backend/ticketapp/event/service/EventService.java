package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.common.response.EventStatsResponseDTO;
import org.core.backend.ticketapp.common.response.EventTicketStatsDTO;
import org.core.backend.ticketapp.event.dao.EventResponseDTO;
import org.core.backend.ticketapp.event.dto.AssignCategoryToEventRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventStatRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EventService {
    Event create(final EventCreateRequestDTO requestDTO) throws Exception;

    List<Event> getAll();

    org.core.backend.ticketapp.common.dto.Page<EventResponseDTO> searchEvents(final EventFilterRequestDTO filterRequest);

    Event getById(final UUID id);

    List<Event> getAllByIds(final List<UUID> id);

    Page<Event> getByEventTicketType(EventTicketType eventTicketType, Pageable pageable);

    void delete(final UUID id);

    Event update(final EventUpdateRequestDTO requestDTO);

    Event assignCategory(AssignCategoryToEventRequestDTO request);

    EventTicketStatsDTO getEventTicketStats(UUID eventId);

    EventStatsResponseDTO getEventStats(final EventStatRequestDTO request);

    Event save(Event event);
}
