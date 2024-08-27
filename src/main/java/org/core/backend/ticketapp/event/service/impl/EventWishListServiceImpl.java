package org.core.backend.ticketapp.event.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.request.events.wishlist.CreateEventWishListDTO;
import org.core.backend.ticketapp.event.entity.EventWishList;
import org.core.backend.ticketapp.event.repository.EventWishListRepository;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.event.service.EventWishListService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public record EventWishListServiceImpl(EventWishListRepository repository,
                                       EventService eventService,
                                       JwtTokenUtil jwtTokenUtil
) implements EventWishListService {
    @Override
    public <R> EventWishList create(R request) {
        final var req = (CreateEventWishListDTO) request;
        final var event = eventService.getById(req.getEventId());
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var existingEventWishList = repository.getByEventIdAndUserId(event.getId(), userId);
        if (!existingEventWishList.isEmpty()) {
            throw new ApplicationException(400, "already_exists", "Event already exists as wishlist!");
        }
        final var eventWishList = new EventWishList(event.getId(), userId);
        eventWishList.setDateCreated(LocalDateTime.now());
        return repository.save(eventWishList);
    }

    @Override
    public void delete(UUID id) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        UserUtils.canAccessResource(userId);
        final var eventWishList = repository.getById(id, userId).orElseThrow(() -> new ApplicationException(404, "not_found", "Event wishlist not found!"));
        eventWishList.setDeleted(true);
        eventWishList.setDateModified(LocalDateTime.now());
        repository.save(eventWishList);
    }

    @Override
    public EventWishList getById(UUID id) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        UserUtils.canAccessResource(userId);
        return repository.getById(id, userId).orElseThrow(() -> new ApplicationException(404, "not_found", "Event wishlist not found!"));
    }

    @Override
    public Page<EventWishList> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<EventWishList> getUserWisList(final Pageable pageable) {
        final var user = jwtTokenUtil.getUser();
        return repository.findAll(user.getUserId(), pageable);
    }
}
