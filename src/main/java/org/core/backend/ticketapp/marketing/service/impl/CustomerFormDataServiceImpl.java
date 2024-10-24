package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.marketing.dto.formdata.CreateCustomerFormDataRequest;
import org.core.backend.ticketapp.marketing.dto.formdata.UpdateCustomerFormDataRequest;
import org.core.backend.ticketapp.marketing.entity.CustomerFormData;
import org.core.backend.ticketapp.marketing.entity.FormData;
import org.core.backend.ticketapp.marketing.repository.CustomerFormDataRepository;
import org.core.backend.ticketapp.marketing.repository.FormDataRepository;
import org.core.backend.ticketapp.marketing.service.CustomerFormDataService;
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
public class CustomerFormDataServiceImpl implements CustomerFormDataService {
    private final CustomerFormDataRepository repository;
    private final FormDataRepository formDataRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public <R> CustomerFormData create(final R request) {
        final var record = modelMapper.map(request, CustomerFormData.class);
        final var tempRequest = modelMapper.map(request, CreateCustomerFormDataRequest.class);
        record.setUserId(jwtTokenUtil.getUser().getUserId());
        record.setTenantId(jwtTokenUtil.getUser().getTenantId());
        record.setDateCreated(LocalDateTime.now());
        final var formData = getByCode(tempRequest.getCode());
        record.setUserId(formData.getUserId());
        return repository.save(record);
    }

    @Override
    public Page<CustomerFormData> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public <R> CustomerFormData update(final R request) {
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
    public CustomerFormData getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }

    private FormData getByCode(String code) {
        return formDataRepository.findByCode(code).orElseThrow(ApplicationExceptionUtils::notFound);
    }
}
