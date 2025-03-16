package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.dto.formdata.UpdateCustomerFormDataRequest;
import org.core.backend.ticketapp.marketing.entity.SponsoredOffer;
import org.core.backend.ticketapp.marketing.repository.SponsoredOfferRepository;
import org.core.backend.ticketapp.marketing.service.SponsoredOfferService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SponsoredOfferServiceImpl implements SponsoredOfferService {
    private final SponsoredOfferRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public <R> SponsoredOffer create(final R request) {
        final var record = modelMapper.map(request, SponsoredOffer.class);
        record.setUserId(jwtTokenUtil.getUser().getUserId());
        record.setTenantId(jwtTokenUtil.getUser().getTenantId());
        record.setDateCreated(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public Page<SponsoredOffer> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public <R> SponsoredOffer update(final R request) {
        final var requestData = modelMapper.map(request, UpdateCustomerFormDataRequest.class);
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
    public SponsoredOffer getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }

    @Override
    public Optional<SponsoredOffer> findByCode(final String code) {
        return repository.findByCode(code);

    }
}
