package org.core.backend.ticketapp.event.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.event.dto.CreateEventSeatSectionDTO;
import org.core.backend.ticketapp.event.dto.EventSeatSectionUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionRepository;
import org.core.backend.ticketapp.event.service.EventSeatSectionService;
import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EventSeatSectionServiceImpl implements EventSeatSectionService, IService<EventSeatSection> {
    private EventRepository eventRepository;
    private EventSeatSectionRepository seatSectionRepository;
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public List<EventSeatSection> create(final CreateEventSeatSectionDTO request) {
        final var eventSeatSections = request.seatSections().stream().map(seatSectionDTO -> {
            final var seatSection = new EventSeatSection();
            seatSection.setType(seatSectionDTO.type());
            seatSection.setUserId(jwtTokenUtil.getUser().getUserId());
            seatSection.setDateCreated(LocalDateTime.now());
            seatSection.setId(UUID.randomUUID());
            seatSection.setPrice(seatSectionDTO.price());
            seatSection.setCapacity(seatSectionDTO.capacity());
            seatSection.setEventId(request.eventId());
            seatSection.setApprovalStatus(ApprovalStatus.APPROVED);
            return seatSection;
        }).collect(Collectors.toList());
        return seatSectionRepository.saveAll(eventSeatSections);
    }

    @Override
    public EventSeatSection getById(final UUID id) {
        return seatSectionRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(404, "not_found", "Event seat section not found"));
    }

    @Override
    @Transactional
    public EventSeatSection update(final EventSeatSectionUpdateRequestDTO seatSectionDTO) {
        final var seatSection = getById(seatSectionDTO.id());
        final var userId = jwtTokenUtil.getUser().getUserId();
        UserUtils.canAccessResource(userId);
        final var seatSectionReq = seatSectionDTO.seatSection();
        seatSection.setType(seatSectionReq.type());
        seatSection.setUserId(userId);
        seatSection.setDateModified(LocalDateTime.now());
        seatSection.setModifiedBy(userId);
        seatSection.setPrice(seatSectionReq.price());
        seatSection.setCapacity(seatSectionReq.capacity());
        seatSection.setApprovalStatus(ApprovalStatus.APPROVED);
        return seatSectionRepository.save(seatSection);
    }

    public void delete(UUID id) {
        EventSeatSection eventSeatSection = seatSectionRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(400, "not_found", "Event seat section not found!"));
        eventSeatSection.setDeleted(true);
        seatSectionRepository.save(eventSeatSection);
    }
}
