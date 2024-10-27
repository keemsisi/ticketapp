package org.core.backend.ticketapp.marketing.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.marketing.dto.event_promotion.CreateEventPromotionRequest;
import org.core.backend.ticketapp.marketing.dto.event_promotion.UpdateEventPromotionRequest;
import org.core.backend.ticketapp.marketing.entity.EventPromotion;
import org.core.backend.ticketapp.marketing.service.EventPromotionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/event-promotions")
@AllArgsConstructor
public class EventPromotionController {
    private final EventPromotionService service;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EventPromotion>> create(@Validated @RequestBody CreateEventPromotionRequest request) throws Exception {
        final var result = service.create(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Resource created successfully", result), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EventPromotion>> getById(final @PathVariable UUID id) {
        final var result = service.getById(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully fetched resource", result));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        final var result = PagedMapperUtil.map(service.getAll(pageable));
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully fetched resource", result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EventPromotion>> deleteById(final @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully deleted resource!", null));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EventPromotion>> update(final @RequestBody UpdateEventPromotionRequest request) {
        final var result = service.update(request);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully updated resource!", result));
    }
}
