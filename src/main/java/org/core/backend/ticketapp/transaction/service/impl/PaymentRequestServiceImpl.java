package org.core.backend.ticketapp.transaction.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ApplicationExceptionUtils;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.dto.CreatePaymentRequestDTO;
import org.core.backend.ticketapp.transaction.dto.payment_request.UpdatePaymentRequestRequestDTO;
import org.core.backend.ticketapp.transaction.entity.request.PaymentRequest;
import org.core.backend.ticketapp.transaction.repository.PaymentRequestRepository;
import org.core.backend.ticketapp.transaction.service.PaymentRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentRequestServiceImpl implements PaymentRequestService {
    private final PaymentRequestRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;
    private final EventService eventService;
    private final OrderService orderService;

    @Override
    public <R> PaymentRequest create(final R request) {
        final var user = jwtTokenUtil.getUser();
        final var record = modelMapper.map(request, PaymentRequest.class);
        final var requestDto = ((CreatePaymentRequestDTO) (request));
        if (record.getType().isEventSettlement()) {
            final var event = eventService.getById(requestDto.getId());
            if (!event.getUserId().equals(user.getUserId()) || !event.getTenantId().equals(user.getTenantId())) {
                throw new ApplicationException(403, "not_allowed", "Only event owner can request");
            }
            final var totalEventAmount = getTotalEventOrderAmount(event.getId());
            record.setEventId(event.getId());
            record.setTotalAmount(totalEventAmount);
        } else if (requestDto.getType().isWalletWithdrawal()) {
            //not revalidating to avoid overhead
            record.setWalletId(requestDto.getId());//this is validated somewhere else before coming here
            record.setTotalAmount(requestDto.getAmount());
        }
        record.setId(UUID.randomUUID());
        record.setUserId(user.getUserId());
        record.setTenantId(user.getTenantId());
        record.setDateCreated(LocalDateTime.now());
        record.setRequestDate(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public Page<PaymentRequest> getAll(final Pageable pageable) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        return Objects.nonNull(userId) ? repository.findAll(userId, pageable) : repository.findAll(pageable);
    }

    @Override
    public <R> PaymentRequest update(final R request) {
        final var requestData = modelMapper.map(request, UpdatePaymentRequestRequestDTO.class);
        final var id = requestData.getId();
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var record = repository.findByIdAndUserId(id, userId).orElseThrow(ApplicationExceptionUtils::notFound);
        BeanUtils.copyProperties(requestData, record);
        record.setModifiedBy(userId);
        record.setDateModified(LocalDateTime.now());
        return repository.save(record);
    }

    @Override
    public void delete(final UUID id) {
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var record = repository.findByIdAndUserId(id, userId).orElseThrow(ApplicationExceptionUtils::notFound);
        record.setDeleted(true);
        record.setDateModified(LocalDateTime.now());
        record.setModifiedBy(jwtTokenUtil.getUser().getUserId());
        repository.save(record);
    }

    @Override
    public PaymentRequest getById(final UUID id) {
        return repository.findById(id).orElseThrow(ApplicationExceptionUtils::notFound);
    }

    @Override
    public PaymentRequest update(final PaymentRequest paymentRequest) {
        return repository.save(paymentRequest);
    }

    private BigDecimal getTotalEventOrderAmount(final UUID eventId) {
        return orderService.getTotalEventOrderAmount(eventId);
    }
}
