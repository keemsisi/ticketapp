package org.core.backend.ticketapp.event.controller;

import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.PagedMapperUtil;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.request.events.wishlist.CreateEventWishListDTO;
import org.core.backend.ticketapp.event.entity.EventWishList;
import org.core.backend.ticketapp.event.service.EventWishListService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/event-wishlists")
public record EventWishListController(EventWishListService eventWishListService) implements ICrudController {

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EventWishList>> create(@RequestBody CreateEventWishListDTO request) {
        return ResponseEntity.ok(new GenericResponse<>("00", "Created Successfully", eventWishListService.create(request)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        eventWishListService.delete(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Deleted Successfully", null));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(new GenericResponse<>("00", "Fetched Successfully", eventWishListService.getById(id)));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(Pageable pageable) {
        final var data = eventWishListService.getUserWisList(pageable);
        final var response = PagedMapperUtil.map(data);
        return ResponseEntity.ok(
                new GenericResponse<>("00", "Fetched Successfully", response));
    }

}
