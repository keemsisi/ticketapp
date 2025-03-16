package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.dto.social.CreateSocialLinksRequest;
import org.core.backend.ticketapp.marketing.dto.social.FilterSearchSocialMediaLinksRequest;
import org.core.backend.ticketapp.marketing.dto.social.UpdateSocialLinksRequest;
import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;
import org.core.backend.ticketapp.marketing.repository.SocialMediaLinksAdvertisementRepository;
import org.core.backend.ticketapp.marketing.service.SocialMediaLinkAdvertisementService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SocialMediaLinkAdvertisementServiceImpl implements SocialMediaLinkAdvertisementService {
    private final SocialMediaLinksAdvertisementRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public <R> List<SocialMediaLinkAdvertisement> createAll(final R request) {
        final var _request = modelMapper.map(request, CreateSocialLinksRequest.class);
        final var newRecord = new ArrayList<SocialMediaLinkAdvertisement>();
        _request.getMedia().forEach(media -> {
            final var record = new SocialMediaLinkAdvertisement();
            record.setUserId(jwtTokenUtil.getUser().getUserId());
            record.setTenantId(jwtTokenUtil.getUser().getTenantId());
            record.setSocialMediaType(media.getSocialMediaType());
            record.setHandle(media.getHandle());
            record.setProfileLink(media.getProfileLink());
            record.setDateCreated(LocalDateTime.now());
            newRecord.add(record);
        });
        return repository.saveAll(newRecord);
    }

    @Override
    public Page<SocialMediaLinkAdvertisement> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public Page<SocialMediaLinkAdvertisement> getAll(final FilterSearchSocialMediaLinksRequest request, final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        if (Objects.nonNull(request.tenantId())) {
            return repository.findAllByTenantId(request.tenantId(), pageable);
        }
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public <R> SocialMediaLinkAdvertisement update(final R request) {
        final var requestData = modelMapper.map(request, UpdateSocialLinksRequest.class);
        final var id = requestData.getId();
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var record = repository.findById(id, userId).orElseThrow(ApplicationExceptionUtils::notFound);
        BeanUtils.copyProperties(requestData, record);
        record.setModifiedBy(userId);
        record.setDateModified(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public void delete(final UUID id) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var record = repository.findById(id, userId).orElseThrow(ApplicationExceptionUtils::notFound);
        record.setDeleted(true);
        record.setDateModified(LocalDateTime.now());
        record.setModifiedBy(jwtTokenUtil.getUser().getUserId());
        repository.save(record);
    }

    @Override
    public SocialMediaLinkAdvertisement getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }
}
