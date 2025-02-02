package org.core.backend.ticketapp.marketing.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.marketing.dto.social.CreateSocialLinksRequest;
import org.core.backend.ticketapp.marketing.dto.social.FollowUserSocialLinkRequest;
import org.core.backend.ticketapp.marketing.dto.social.UpdateSocialLinksRequest;
import org.core.backend.ticketapp.marketing.entity.ExternalAppFollower;
import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;
import org.core.backend.ticketapp.marketing.service.SocialMediaLinkAdvertisementService;
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
@RequestMapping("/api/v1/social-media-links/advertisements")
@AllArgsConstructor
public class SocialMediaLinksAdvertisementController {
    private final SocialMediaLinkAdvertisementService service;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SocialMediaLinkAdvertisement>> create(@Validated @RequestBody CreateSocialLinksRequest request) throws Exception {
//        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var result = service.create(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Social links created successfully", result), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SocialMediaLinkAdvertisement>> getById(final @PathVariable UUID id) {
        final var result = service.getById(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully fetched social link", result));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        final var result = PagedMapperUtil.map(service.getAll(pageable));
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully fetched social links", result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SocialMediaLinkAdvertisement>> deleteById(final @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully deleted social link!", null));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<SocialMediaLinkAdvertisement>> update(final @RequestBody UpdateSocialLinksRequest request) {
        final var result = service.update(request);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Successfully updated social link!", result));
    }


    @RequestMapping(value = "/follow", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<ExternalAppFollower>> follow(final @RequestBody FollowUserSocialLinkRequest request) {
        final var result = service.follow(request);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Follow user social link was successful", result));
    }
}
