package org.core.backend.ticketapp.passport.service.core.apconfig;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.dto.formdata.UpdateCustomerFormDataRequest;
import org.core.backend.ticketapp.passport.entity.ApplicationConfig;
import org.core.backend.ticketapp.passport.repository.ApplicationConfigRepository;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApplicationConfigServiceImpl implements ApplicationConfigService {
    private final ModelMapper modelMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final ApplicationConfigRepository repository;

    @Override
    public <R> ApplicationConfig create(final R request) {
        final var record = modelMapper.map(request, ApplicationConfig.class);
        record.setUserId(jwtTokenUtil.getUser().getUserId());
        record.setTenantId(jwtTokenUtil.getUser().getTenantId());
        record.setDateCreated(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public Page<ApplicationConfig> getAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public <R> ApplicationConfig update(final R request) {
        final var requestData = modelMapper.map(request, UpdateCustomerFormDataRequest.class);
        final var id = requestData.getId();
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var record = repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
        BeanUtils.copyProperties(requestData, record);
        record.setModifiedBy(userId);
        record.setDateModified(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public void delete(final UUID id) {
        final var record = repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
        record.setModifiedBy(jwtTokenUtil.getUser().getUserId());
        record.setDateModified(LocalDateTime.now());
        record.setDeleted(true);
        repository.save(record);
    }

    @Override
    public ApplicationConfig getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }

}
