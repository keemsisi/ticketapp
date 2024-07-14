package org.core.backend.ticketapp.event.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.dto.EventSeatSectionCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventSeatSectionUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionRepository;
import org.core.backend.ticketapp.event.service.EventSeatSectionService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EventSeatSectionServiceImpl implements EventSeatSectionService {
    private EventRepository eventRepository;
    private EventSeatSectionRepository seatSectionRepository;
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public EventSeatSection create(EventSeatSectionCreateRequestDTO seatSectionDTO) {
        final var seatSection = new EventSeatSection();
        seatSection.setType(seatSectionDTO.name());
        seatSection.setUserId(jwtTokenUtil.getUser().getUserId());
        seatSection.setDateCreated(LocalDateTime.now());
        seatSection.setId(UUID.randomUUID());
        seatSection.setPrice(seatSectionDTO.price());
        seatSection.setCapacity(seatSectionDTO.capacity());
        seatSection.setAcquired(seatSectionDTO.capacity());
        seatSection.setApprovalStatus(ApprovalStatus.PENDING);
        return seatSectionRepository.save(seatSection);
    }

    @Override
    public EventSeatSection getById(UUID id) {
        EventSeatSection seatSection = seatSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event seat section not found", id.toString()));
        return seatSection;
    }

    @Override
    @Transactional
    public EventSeatSection update(UUID id, EventSeatSectionUpdateRequestDTO seatSectionDTO) {
        Optional<EventSeatSection> optionalSeatSection = seatSectionRepository.findById(id);
        if (optionalSeatSection.isPresent()) {
            EventSeatSection seatSection = optionalSeatSection.get();
            seatSection.setType(seatSectionDTO.name());
            seatSection.setPrice(seatSectionDTO.price());
            seatSection.setCapacity(seatSectionDTO.capacity());
            seatSection.setAcquired(seatSectionDTO.capacity());
            seatSection.setApprovalStatus(seatSectionDTO.approvalStatus());
            seatSection.setDateModified(LocalDateTime.now());
            return seatSectionRepository.save(seatSection);
        } else {
            throw new IllegalArgumentException("Seat section not found with id: " + id);
        }
    }

    public void delete(UUID id) {
        EventSeatSection eventSeatSection = seatSectionRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(400, "not_found", "Event seat section not found!"));
        eventSeatSection.setDeleted(true);
        seatSectionRepository.save(eventSeatSection);
    }
}
