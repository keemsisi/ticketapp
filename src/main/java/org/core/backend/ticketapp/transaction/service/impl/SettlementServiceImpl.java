package org.core.backend.ticketapp.transaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.core.backend.ticketapp.common.enums.OrderType;
import org.core.backend.ticketapp.common.enums.Status;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.service.core.PaymentProcessorType;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.core.backend.ticketapp.transaction.dto.SettlementRequestDTO;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.core.backend.ticketapp.transaction.entity.PaymentGatewayMeta;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.SettlementService;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.core.backend.ticketapp.transaction.service.payment.PaymentProcessorService;
import org.core.backend.ticketapp.transaction.service.payment.paystack.dto.TransferRequestDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private final BankService bankService;
    private final PaymentProcessorService paymentProcessorService;
    private final ObjectMapper objectMapper;
    private final String PAYSTACK_SOURCE = "balance";
    private final AppConfigs appConfigs;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Transaction transfer(final SettlementRequestDTO request) throws JsonProcessingException {
        final var bankAccountDetails = bankService.getByUserId(request.getUserId());
        return switch (appConfigs.defaultPaymentProcessor) {
            case PAYSTACK -> processAndBuildPayStackTransaction(bankAccountDetails, request);
        };
    }

    private Transaction processAndBuildPayStackTransaction(final BankAccountDetails bankAccountDetails, final SettlementRequestDTO request) throws JsonProcessingException {
        final var user = jwtTokenUtil.getUser();
        final var transferRequest = getTransferRequestDTO(bankAccountDetails, request);
        final var processorResponse = paymentProcessorService.transfer(transferRequest, bankAccountDetails);
        final var jsonResponse = objectMapper.writeValueAsString(processorResponse);
        final var transaction = new Transaction();
        transaction.setDateCreated(LocalDateTime.now());
        transaction.setType(OrderType.EVENT_SETTLEMENT);
        transaction.setStatus(Status.PENDING);
        transaction.setComment(transferRequest.getReason());
        final var timestamp = System.currentTimeMillis();
        transaction.setReference(String.format("%s_%s_%s", timestamp, bankAccountDetails.getAccountNumber(),
                bankAccountDetails.getReference()));
        transaction.setCreatedBy(user.getUserId());
        transaction.setGateWayMeta(PaymentGatewayMeta.builder().gatewayResponse(jsonResponse).build());
        transaction.setUserId(bankAccountDetails.getUserId());
        transaction.setTenantId(bankAccountDetails.getTenantId());
        return transactionService.save(transaction);
    }

    private @NotNull TransferRequestDTO getTransferRequestDTO(final BankAccountDetails bankAccountDetails, final SettlementRequestDTO request) {
        final var event = Objects.nonNull(request.getEventId()) ? eventService.getById(request.getEventId()) : null;
        final var user = coreUserService.getUserById(bankAccountDetails.getUserId())
                .orElseThrow(() -> new ApplicationException(404, "not_found", "User not found!"));
        if (Objects.nonNull(event) && !event.getTenantId().equals(bankAccountDetails.getTenantId()))
            throw new ApplicationException(400, "bad_request", "Recipient is not the owner of the event!");
        if (Objects.nonNull(event) && !user.getAccountType().isIndividualOrOrganizationMerchantOwner())
            throw new ApplicationException(400, "bad_request", "Oops! Only Owner can request for event settlement payment!");
        return getRequestDTO(bankAccountDetails, request);
    }

    private @NotNull TransferRequestDTO getRequestDTO(BankAccountDetails bankAccountDetails, SettlementRequestDTO request) {
        final var transferRequest = new TransferRequestDTO();
        transferRequest.setAmount(request.getAmount());
        transferRequest.setRecipient(bankAccountDetails.getReference());
        transferRequest.setSource(PAYSTACK_SOURCE);
        transferRequest.setReason(String.format("Event %s payment to %s",
                request.getTransactionType().name().toLowerCase(),
                bankAccountDetails.getAccountName()));
        transferRequest.setPaymentProcessorType(PaymentProcessorType.PAYSTACK);
        return transferRequest;
    }
}
