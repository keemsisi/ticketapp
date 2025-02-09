package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.dto.social.UpdateSocialLinksRequest;
import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;
import org.core.backend.ticketapp.marketing.repository.ExternalAppFollowerRepository;
import org.core.backend.ticketapp.marketing.repository.SocialMediaLinksAdvertisementRepository;
import org.core.backend.ticketapp.marketing.service.SocialMediaLinkAdvertisementService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.service.wallet.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SocialMediaLinkAdvertisementServiceImpl implements SocialMediaLinkAdvertisementService {
    private final SocialMediaLinksAdvertisementRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public <R> SocialMediaLinkAdvertisement create(final R request) {
        final var record = modelMapper.map(request, SocialMediaLinkAdvertisement.class);
        record.setUserId(jwtTokenUtil.getUser().getUserId());
        record.setTenantId(jwtTokenUtil.getUser().getTenantId());
        record.setDateCreated(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public Page<SocialMediaLinkAdvertisement> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
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
