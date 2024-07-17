package org.core.backend.ticketapp.event.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.dto.EventCategoryCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCategoryUpdateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.entity.EventCategory;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventCategoryRepository;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.service.EventCategoryService;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@AllArgsConstructor
public class EventCategoryServiceImpl implements EventCategoryService {

    private EventRepository eventRepository;
    private ModelMapper modelMapper;
    private EventCategoryRepository categoryRepository;

    public List<EventCategory> getAll() {
        List< EventCategory> categories = categoryRepository.findAll();
        return categories.stream()
                .map((category) -> modelMapper.map(category, EventCategory.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventCategory create(EventCategoryCreateRequestDTO categoryDTO) {
        final var category = convertToEntity(categoryDTO);
        categoryRepository.save(category);
        return category;
    }

    public EventCategory getById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(404, "not_found", "Event category not found!"));
    }

    public EventCategory update(final EventCategoryUpdateRequestDTO request) {
        EventCategory eventCategory = categoryRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Event category not found", id.toString()));
        eventCategory.setName(request.name());
        eventCategory.setDescription(request.description());
        return categoryRepository.save(eventCategory);
    }

    @Override
    public Optional<EventCategory> getByName(final String categoryName) {
        return categoryRepository.getByName(categoryName);
    }

    public void delete(UUID id) {
        EventCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(404, "not_found", "Resource not found!"));
        categoryRepository.delete(category);
    }

    private EventCategoryCreateRequestDTO convertToDTO(EventCategory eventCategory) {
        return modelMapper.map(eventCategory, EventCategoryCreateRequestDTO.class);
    }

    private EventCategory convertToEntity(EventCategoryCreateRequestDTO categoryDTO) {
        return modelMapper.map(categoryDTO, EventCategory.class);
    }
}
