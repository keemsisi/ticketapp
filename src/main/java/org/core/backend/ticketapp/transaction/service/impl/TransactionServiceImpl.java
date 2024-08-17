package org.core.backend.ticketapp.transaction.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.enums.OrderStatus;
import org.core.backend.ticketapp.common.enums.Status;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.dto.InitTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentInitResponseDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentVerificationResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.entity.PaymentGatewayMeta;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.repository.TransactionRepository;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_INITIALIZE_PAY;
import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_VERIFY;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final RestTemplate restTemplate;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppConfigs appConfigs;
    private final ObjectMapper objectMapper;

    @Override
    public Page<Transaction> getAll(final Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Order initializePayment(final InitTransactionRequestDTO request) {
        ResponseEntity<PaymentInitResponseDTO> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + appConfigs.payStackApiKey);
            headers.set("Content-Type", "application/json");
            final var entity = new HttpEntity<>(request, headers);
            response = restTemplate.exchange(PAYSTACK_INITIALIZE_PAY, HttpMethod.POST, entity, PaymentInitResponseDTO.class);
            if (response.getStatusCode().isError()) {
                throw new ApplicationException(400, "init_payment_failed", "Failed to init payment");
            }
            return getOrder(request, Objects.requireNonNull(response.getBody()));
        } catch (Throwable e) {
            log.error(">>> Error Occurred while initiating transaction", e);
        }
        throw new ApplicationException(400, "init_payment_failed", "Failed to init payment with checkout link!");
    }

    private Order getOrder(final InitTransactionRequestDTO initRequest, final PaymentInitResponseDTO response) {
        final var data = response.getData();
        final var order = new Order();
        order.setEventId(initRequest.getEventId());
        order.setQuantity(ObjectUtils.defaultIfNull(initRequest.getQuantity(), 1));
        order.setUserId(jwtTokenUtil.getUser().getUserId());
        order.setAmount(initRequest.getAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentLink(data.getAuthorizationUrl());
        order.setCode(data.getAccessCode());
        order.setTenantId(jwtTokenUtil.getUser().getTenantId());
        order.setReference(data.getReference());
        return orderService.save(order);
    }

    @Override
    @Transactional
    public Transaction verifyPayment(final TransactionVerifyRequestDTO verifyRequestDTO) {
        final var order = orderService.getById(verifyRequestDTO.getOrderId());
        try {
            final var headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + appConfigs.payStackApiKey);
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            final var response = restTemplate.exchange(String.format(PAYSTACK_VERIFY, order.getReference()),
                    HttpMethod.GET, entity, PaymentVerificationResponseDTO.class);
            if (response.getStatusCode().isError()) {
                log.error(">>> Payment verification was not successful for request {} ", verifyRequestDTO);
                final var errorCode = System.nanoTime();
                throw new ApplicationException(400, "req_failed",
                        String.format("Failed to verify payment, please try again later or contact support with code: %s", errorCode));
            } else {
                final var data = Objects.requireNonNull(Objects.requireNonNull(response.getBody()).getData());
                final var gatewayResponse = objectMapper.writeValueAsString(response.getBody());
                final var meta = PaymentGatewayMeta.builder()
                        .paidAt(data.getPaidAt())
                        .createdAt(data.getCreatedAt())
                        .channel(data.getChannel())
                        .currency(data.getCurrency())
                        .gateway("PayStack")
                        .gatewayResponse(gatewayResponse)
                        .build();
                //TODO: fetch the transaction if it already exists else create a new transaction
                final var transaction = Transaction.builder()
                        .userId(order.getUserId())
                        .reference(order.getReference())
                        .amount(order.getAmount())
                        .orderId(verifyRequestDTO.getOrderId())
                        .status(Objects.isNull(meta.getPaidAt()) ? Status.PENDING : Status.PAID)
                        .gateWayMeta(meta)
                        .build();
                transaction.setTenantId(order.getTenantId());
                return transactionRepository.save(transaction);
            }
        } catch (Exception e) {
            log.error(">>> Error Occurred while initiating transaction", e);
            final var errorCode = System.nanoTime();
            throw new ApplicationException(400, "req_failed", String.format("Could not verify payment, please try again later or contact support with code: %s", errorCode));
        }
    }
}
