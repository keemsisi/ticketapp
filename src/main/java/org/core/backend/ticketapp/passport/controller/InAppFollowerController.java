package org.core.backend.ticketapp.passport.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.marketing.dto.social.CreateInAppFollowerRequest;
import org.core.backend.ticketapp.marketing.dto.social.UpdateInAppFollowerRequest;
import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.entity.InAppFollower;
import org.core.backend.ticketapp.passport.service.InAppFollowerService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
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
@RequestMapping("/api/v1/in-app-follower")
@AllArgsConstructor
public class InAppFollowerController {
    private final InAppFollowerService service;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<InAppFollower>> create(@Validated @RequestBody CreateInAppFollowerRequest request) throws Exception {
        final var result = service.create(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Resource created successfully", result), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<InAppFollower>> getById(final @PathVariable UUID id) {
        final var result = service.getById(id);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully fetched social link", result));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Page<?>>> getAll(final FilterInAppFollowerRequestDTO filter, final Pageable pageable) {
        final var result = service.getAllV1(filter, pageable);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully fetched resource", result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<InAppFollower>> deleteById(final @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully deleted resource!", null));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<InAppFollower>> update(final @RequestBody UpdateInAppFollowerRequest request) {
        final var result = service.update(request);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully updated resource!", result));
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Page<?>>> getFollowers(final FilterInAppFollowerRequestDTO request, final Pageable pageable) {
        final var result = service.getAllV2(request, pageable);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully updated resource!", result));
    }

}
