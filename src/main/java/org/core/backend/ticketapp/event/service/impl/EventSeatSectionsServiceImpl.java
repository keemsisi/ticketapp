package org.core.backend.ticketapp.event.service.impl;

import org.core.backend.ticketapp.event.dto.EventSeatSectionsCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventSeatSectionsUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventSeatSections;
import org.core.backend.ticketapp.event.service.EventSeatSectionsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventSeatSectionsServiceImpl implements EventSeatSectionsService {

    @Override
    public EventSeatSections create(EventSeatSectionsCreateRequestDTO requestDTO) {
        return null;
    }

    @Override
    public EventSeatSections getById(UUID id) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public EventSeatSections update(EventSeatSectionsUpdateRequestDTO requestDTO) {
        return null;
    }
}
