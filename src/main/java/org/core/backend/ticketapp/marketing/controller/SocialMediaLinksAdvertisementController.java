package org.core.backend.ticketapp.marketing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.marketing.dto.social.CreateSocialLinksRequest;
import org.core.backend.ticketapp.marketing.dto.social.FollowUserSocialLinkRequest;
import org.core.backend.ticketapp.marketing.dto.social.UpdateSocialLinksRequest;
import org.core.backend.ticketapp.marketing.entity.ExternalAppFollower;
import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;
import org.core.backend.ticketapp.marketing.service.SocialMediaLinkAdvertisementService;
import org.core.backend.ticketapp.marketing.service.impl.ExternalAppFollowerService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
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
    private final ExternalAppFollowerService followerService;
    private final ActivityLogProcessorUtils activityLogProcessorUtils;
    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<SocialMediaLinkAdvertisement>> create(@Validated @RequestBody CreateSocialLinksRequest request) throws Exception {
        final var result = service.create(request);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), SocialMediaLinkAdvertisement.class.getTypeName(), null, objectMapper.writeValueAsString(result),
                "Initiated a request to follow create social media link advert");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Social links created successfully", result), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<SocialMediaLinkAdvertisement>> getById(final @PathVariable UUID id) {
        final var result = service.getById(id);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully fetched social link", result));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        final var result = PagedMapperUtil.map(service.getAll(pageable));
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully fetched social links", result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<SocialMediaLinkAdvertisement>> deleteById(final @PathVariable UUID id) {
        service.delete(id);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), SocialMediaLinkAdvertisement.class.getTypeName(), null, null,
                "Initiated a request to follow delete social media link advert");
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully deleted social link!", null));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<SocialMediaLinkAdvertisement>> update(final @RequestBody UpdateSocialLinksRequest request) throws JsonProcessingException {
        final var result = service.update(request);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), SocialMediaLinkAdvertisement.class.getTypeName(), null, objectMapper.writeValueAsString(result),
                "Initiated a request to follow update social media link advert");
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully updated social link!", result));
    }


    @RequestMapping(value = "/follow", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<ExternalAppFollower>> follow(final @RequestBody FollowUserSocialLinkRequest request) throws JsonProcessingException {
        final var user = jwtTokenUtil.getUser();
        request.setUserId(user.getUserId());
        final var result = followerService.follow(request);
        activityLogProcessorUtils.processActivityLog(user.getUserId(), ExternalAppFollower.class.getTypeName(), null, objectMapper.writeValueAsString(result),
                "Initiated a request to follow a user");
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Follow user social link was successful", result));
    }
}
