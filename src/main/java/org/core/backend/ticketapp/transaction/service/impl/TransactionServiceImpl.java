package org.core.backend.ticketapp.transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.core.backend.ticketapp.common.enums.PricingPlanType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.transaction.dto.InitTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentInitResponseDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentVerificationResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.repository.TransactionRepository;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_INITIALIZE_PAY;
import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_VERIFY;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final CoreUserService userService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${paystack.secret.key}")
    private String secretKey;

    @Override
    public Page<Transaction> getAll(final Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    public PaymentInitResponseDTO initializePayment(final InitTransactionRequestDTO initializePaymentDto) {
        ResponseEntity<PaymentInitResponseDTO> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + secretKey);
            headers.set("Content-Type", "application/json");
            final var entity = new HttpEntity<>(initializePaymentDto, headers);
            response = restTemplate.exchange(PAYSTACK_INITIALIZE_PAY, HttpMethod.POST, entity, PaymentInitResponseDTO.class);
            if (response.getStatusCode().isError()) {
                throw new ApplicationException(400, "init_payment_failed", "Failed to init payment");
            }
            return response.getBody();
        } catch (Throwable e) {
            log.error(">>> Error Occurred while initiating transaction", e);
        }
        throw new ApplicationException(400, "init_payment_failed", "Failed to init payment with checkout link!");
    }

    @Override
    @Transactional
    public boolean verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) {
        ResponseEntity<PaymentVerificationResponseDTO> response = null;
        Transaction transaction = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + secretKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            response = restTemplate.exchange(PAYSTACK_VERIFY + verifyRequestDTO.getReference(), HttpMethod.GET, entity, PaymentVerificationResponseDTO.class);

            if (response.getStatusCode().isError()) {
                log.error(">>> Payment verification was not successful for request {} ", verifyRequestDTO);
                final var errorCode = System.nanoTime();
                throw new ApplicationException(400, "payment_verification_failed",
                        String.format("Failed to verify payment, please try again later or contact support with code: %s", errorCode));
            } else {
                User user = userService.getUserById(verifyRequestDTO.getUserId()).orElseThrow(() -> new ApplicationException(404, "not_found", "User not found!"));
                PricingPlanType pricingPlanType = PricingPlanType.valueOf(String.valueOf(verifyRequestDTO.getPricingPlan()).toUpperCase());
                //TODO: fetch the transaction if it already exists else create a new transaction
                transaction = Transaction.builder()
                        .id(UUID.randomUUID())
                        .userId(user)
                        .reference(verifyRequestDTO.getReference())
                        .amount(verifyRequestDTO.getAmount())
                        .orderId(verifyRequestDTO.getOrderId())
                        .gatewayResponse(verifyRequestDTO.getGatewayResponse())
                        .paidAt(verifyRequestDTO.getPaidAt())
                        .createdAt(verifyRequestDTO.getPaidAt())
                        .channel(verifyRequestDTO.getChannel())
                        .currency(verifyRequestDTO.getCurrency())
                        .ipAddress(verifyRequestDTO.getIpAddress())
                        .pricingPlan(pricingPlanType)
                        .paymentDate(new Timestamp(System.currentTimeMillis()))
                        .build();
                transactionRepository.save(transaction);
            }
        } catch (Exception e) {
            final var errorCode = System.nanoTime();
            log.error(">>> Error Occurred while initiating transaction", e);
            throw new ApplicationException(400, "payment_verification_failed",
                    String.format("Could not verify payment, please try again later or contact support with code: %s", errorCode));
        }
        return Objects.requireNonNull(response.getBody()).isStatus();
    }
}
