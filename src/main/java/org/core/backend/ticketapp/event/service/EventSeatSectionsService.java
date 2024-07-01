package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.event.dto.EventSeatSectionsCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventSeatSectionsUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventSeatSections;

import java.util.UUID;

public interface EventSeatSectionsService {
    EventSeatSections create(final EventSeatSectionsCreateRequestDTO requestDTO);

    EventSeatSections getById(final UUID id);

    void deleteById(final UUID id);

    EventSeatSections update(final EventSeatSectionsUpdateRequestDTO requestDTO);
}
