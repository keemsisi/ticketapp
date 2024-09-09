package org.core.backend.ticketapp.transaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.core.backend.ticketapp.common.enums.Status;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.core.backend.ticketapp.transaction.dto.TransferRequestDTO;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.core.backend.ticketapp.transaction.entity.PaymentGatewayMeta;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.SettlementService;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.core.backend.ticketapp.transaction.service.payment.PaymentProcessorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Data
@Service
@Transactional
@AllArgsConstructor
public class SettlementServiceImpl implements SettlementService {
    private final TicketService ticketService;
    private final EventService eventService;
    private final CoreUserService coreUserService;
    private final OrderService orderService;
    private final TransactionService transactionService;
    private final BankAccountDetailsService bankAccountDetailsService;
    private final PaymentProcessorService paymentProcessorService;
    private final ObjectMapper objectMapper;
    private final String PAYSTACK_SOURCE = "balance";
    private final AppConfigs appConfigs;

    @Override
    public Transaction transfer(final TransferRequestDTO request) throws JsonProcessingException {
        final var bankAccountDetails = bankAccountDetailsService.getByUserId(request.getUserId());
        return switch (appConfigs.defaultPaymentProcessor) {
            case PAYSTACK -> processAndBuildPayStackTransaction(bankAccountDetails, request);
        };
    }

    private Transaction processAndBuildPayStackTransaction(final BankAccountDetails bankAccountDetails, TransferRequestDTO request) throws JsonProcessingException {
        final var transferRequest = new org.core.backend.ticketapp.transaction.service.payment.paystack.dto
                .TransferRequestDTO();
        transferRequest.setAmount(request.getAmount());
        transferRequest.setRecipient(bankAccountDetails.getReference());
        transferRequest.setSource(PAYSTACK_SOURCE);
        transferRequest.setReason(String.format("Event settlement payment to %s", bankAccountDetails.getAccountName()));
        final var processorResponse = paymentProcessorService.transfer(transferRequest, bankAccountDetails);
        final var jsonResponse = objectMapper.writeValueAsString(processorResponse);
        final var transaction = new Transaction();
        transaction.setDateCreated(LocalDateTime.now());
        transaction.setStatus(Status.PENDING);
        transaction.setComment(transferRequest.getReason());
        final var timestamp = System.currentTimeMillis();
        transaction.setReference(String.format("%s_%s_%s", timestamp, bankAccountDetails.getAccountNumber(),
                bankAccountDetails.getReference()));
        transaction.setGateWayMeta(PaymentGatewayMeta.builder().gatewayResponse(jsonResponse).build());
        transaction.setUserId(bankAccountDetails.getUserId());
        transaction.setTenantId(bankAccountDetails.getTenantId());
        return transactionService.save(transaction);
    }
}
