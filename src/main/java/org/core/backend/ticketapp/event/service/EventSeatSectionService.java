package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.event.dto.CreateEventSeatSectionDTO;
import org.core.backend.ticketapp.event.dto.EventSeatSectionUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventSeatSection;

import java.util.List;
import java.util.UUID;

public interface EventSeatSectionService {
    List<EventSeatSection> create(CreateEventSeatSectionDTO request);

    EventSeatSection getById(final UUID id);

    void delete(final UUID id);

    EventSeatSection update(final EventSeatSectionUpdateRequestDTO requestDTO);
}
