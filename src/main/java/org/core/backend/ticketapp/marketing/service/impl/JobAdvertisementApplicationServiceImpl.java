package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.entity.JobAdvertisementApplication;
import org.core.backend.ticketapp.marketing.repository.JobAdvertisementApplicationRepository;
import org.core.backend.ticketapp.marketing.service.JobAdvertisementApplicationService;
import org.core.backend.ticketapp.marketing.service.JobAdvertisementService;
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
public class JobAdvertisementApplicationServiceImpl implements JobAdvertisementApplicationService {
    private final JobAdvertisementApplicationRepository repository;
    private final JobAdvertisementService advertisementService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public <R> JobAdvertisementApplication create(final R request) {
        final var record = modelMapper.map(request, JobAdvertisementApplication.class);
        final var job = advertisementService.getById(record.getJobId());
        if (job.getDeadlineDate().isBefore(LocalDateTime.now())) {
            throw new ApplicationException(400, "not_allowed", "Application after deadline not allowed");
        }
        record.setJobId(job.getId());
        record.setUserId(jwtTokenUtil.getUser().getUserId());
        record.setTenantId(jwtTokenUtil.getUser().getTenantId());
        record.setDateCreated(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public Page<JobAdvertisementApplication> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public <R> JobAdvertisementApplication update(final R request) {
        final var requestData = modelMapper.map(request, UpdateJobAdvertisementApplicationRequest.class);
        final var job = advertisementService.getById(requestData.getJobId());
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
    public JobAdvertisementApplication getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }
}
