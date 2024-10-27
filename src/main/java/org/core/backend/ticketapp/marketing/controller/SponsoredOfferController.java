package org.core.backend.ticketapp.marketing.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.marketing.dto.sponsored_offer.CreateSponsoredOfferRequest;
import org.core.backend.ticketapp.marketing.dto.sponsored_offer.UpdateSponsoredOfferRequest;
import org.core.backend.ticketapp.marketing.entity.SponsoredOffer;
import org.core.backend.ticketapp.marketing.service.SponsoredOfferService;
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
@RequestMapping("/api/v1/sponsored-offers")
@AllArgsConstructor
public class SponsoredOfferController {
    private final SponsoredOfferService service;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SponsoredOffer>> create(@Validated @RequestBody CreateSponsoredOfferRequest request) throws Exception {
        final var result = service.create(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Resource created successfully", result), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SponsoredOffer>> getById(final @PathVariable UUID id) {
        final var result = service.getById(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully fetched resource", result));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        final var result = PagedMapperUtil.map(service.getAll(pageable));
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully fetched resource", result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SponsoredOffer>> deleteById(final @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully deleted resource!", null));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SponsoredOffer>> update(final @RequestBody UpdateSponsoredOfferRequest request) {
        final var result = service.update(request);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully updated resource!", result));
    }
}
