package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventCreateRequestDTO create(final EventCreateRequestDTO requestDTO);

    List<Event> getAll();

    Page<Event> searchEvents(final EventFilterRequestDTO filterRequest);

    EventCreateRequestDTO getById(final UUID id);

    void delete(final UUID id);

    Event update(final UUID id, final EventUpdateRequestDTO requestDTO);
}
