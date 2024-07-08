package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.event.dto.EventSeatSectionCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventSeatSectionUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventSeatSection;

import java.util.UUID;

public interface EventSeatSectionService {
    EventSeatSection create(final EventSeatSectionCreateRequestDTO requestDTO);

    EventSeatSection getById(final UUID id);

    void delete(final UUID id);

    EventSeatSection update(final UUID id, final EventSeatSectionUpdateRequestDTO requestDTO);
}
