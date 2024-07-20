package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.event.dto.EventCategoryCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCategoryUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.entity.EventCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventCategoryService {
    EventCategory create(final EventCategoryCreateRequestDTO requestDTO);

    List<EventCategory> getAll();

    EventCategory getById(final UUID id);

    Optional<EventCategory> getByName(String eventCategory);

    void delete(final UUID id);

    EventCategory update(final EventCategoryUpdateRequestDTO requestDTO);

}
