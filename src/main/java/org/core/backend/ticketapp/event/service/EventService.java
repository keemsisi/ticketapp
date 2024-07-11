package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventCreateRequestDTO create(final EventCreateRequestDTO requestDTO);

    List<Event> getAll();

    List<Event> searchEvents(final EventFilterRequestDTO requestDTO);

    EventCreateRequestDTO getById(final UUID id);

    void delete(final UUID id);

    Event update(final UUID id, final EventUpdateRequestDTO requestDTO);
}
