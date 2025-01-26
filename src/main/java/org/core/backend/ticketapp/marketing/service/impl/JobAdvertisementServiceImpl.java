package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.dto.job_advertisement.UpdateJobAdvertisementRequest;
import org.core.backend.ticketapp.marketing.entity.JobAdvertisement;
import org.core.backend.ticketapp.marketing.repository.JobAdvertisementRepository;
import org.core.backend.ticketapp.marketing.service.JobAdvertisementService;
import org.core.backend.ticketapp.passport.service.TenantService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
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
public class JobAdvertisementServiceImpl implements JobAdvertisementService {
    private final JobAdvertisementRepository repository;
    private final TenantService tenantService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public <R> JobAdvertisement create(final R request) {
        final var record = modelMapper.map(request, JobAdvertisement.class);
        final var tenantId = jwtTokenUtil.getUser().getTenantId();
        final var tenant = tenantService.getById(tenantId).orElseThrow();
        record.setCompanyImage(tenant.getLogoUrl());
        record.setCompanyName(tenant.getName());
        record.setUserId(jwtTokenUtil.getUser().getUserId());
        record.setTenantId(jwtTokenUtil.getUser().getTenantId());
        record.setDateCreated(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public Page<JobAdvertisement> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public <R> JobAdvertisement update(final R request) {
        final var requestData = modelMapper.map(request, UpdateJobAdvertisementRequest.class);
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
    public JobAdvertisement getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }
}
