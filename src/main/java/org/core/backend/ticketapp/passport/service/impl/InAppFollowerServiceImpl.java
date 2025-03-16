package org.core.backend.ticketapp.passport.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.common.FollowerStatus;
import org.core.backend.ticketapp.marketing.dao.InAppFollowerDAOService;
import org.core.backend.ticketapp.marketing.dto.social.UpdateInAppFollowerRequest;
import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.entity.InAppFollower;
import org.core.backend.ticketapp.passport.repository.InAppFollowerRepository;
import org.core.backend.ticketapp.passport.service.InAppFollowerService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class InAppFollowerServiceImpl implements InAppFollowerService {
    private final InAppFollowerDAOService inAppFollowerDAOService;
    private final InAppFollowerRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public <R> InAppFollower create(final R request) {
        final var record = modelMapper.map(request, InAppFollower.class);
        record.setUserId(jwtTokenUtil.getUser().getUserId());
        record.setTenantId(jwtTokenUtil.getUser().getTenantId());
        record.setDateCreated(LocalDateTime.now());
        record.setStatus(FollowerStatus.FOLLOWED);
        return repository.save(record);
    }

    @Override
    public Page<InAppFollower> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public Page<InAppFollower> getAllV2(final FilterInAppFollowerRequestDTO request, final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        request.setUserId(userId);
        return repository.findAllByUserId(userId, pageable);
    }

    @Override
    public <R> InAppFollower update(final R request) {
        final var requestData = modelMapper.map(request, UpdateInAppFollowerRequest.class);
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
    public InAppFollower getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }
}
