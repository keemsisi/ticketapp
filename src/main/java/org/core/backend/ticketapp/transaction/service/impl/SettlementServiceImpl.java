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
import org.core.backend.ticketapp.passport.service.RedisService;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.service.core.PaymentProcessorType;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.core.backend.ticketapp.transaction.dto.ApprovePaymentRequestDTO;
import org.core.backend.ticketapp.transaction.dto.CreatePaymentRequestDTO;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.core.backend.ticketapp.transaction.entity.PaymentGatewayMeta;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.entity.request.PaymentRequest;
import org.core.backend.ticketapp.transaction.entity.request.PaymentRequestStatus;
import org.core.backend.ticketapp.transaction.service.PaymentRequestService;
import org.core.backend.ticketapp.transaction.service.SettlementService;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.core.backend.ticketapp.transaction.service.payment.PaymentProcessorService;
import org.core.backend.ticketapp.transaction.service.payment.paystack.dto.TransferRequestDTO;
import org.core.backend.ticketapp.transaction.service.wallet.WalletService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
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
    private final BankAccountDetailsService bankAccountDetailsService;
    private final PaymentProcessorService paymentProcessorService;
    private final ObjectMapper objectMapper;
    private final String PAYSTACK_SOURCE = "balance";
    private final AppConfigs appConfigs;
    private final JwtTokenUtil jwtTokenUtil;
    private final PaymentRequestService paymentRequestService;
    private final WalletService walletService;
    private final RedisService redisService;

    @Override
    @Transactional
    public Transaction processApprovedTransfer(final ApprovePaymentRequestDTO request) throws JsonProcessingException {
        if (request.getType().isEventSettlement()) {
            final var paymentRequest = paymentRequestService.getById(request.getPaymentRequestId());
            final var event = eventService.getById(paymentRequest.getEventId());
            final var bankAccountDetails = bankAccountDetailsService.getByUserId(event.getUserId());
            paymentRequest.setEvent(event);
            final var transaction = processTransactionThroughProvider(bankAccountDetails, paymentRequest);
            if (transaction.getStatus().isCompleted()) {
                paymentRequest.setStatus(PaymentRequestStatus.COMPLETED_PAYMENT);
                paymentRequest.setDateModified(LocalDateTime.now());
                paymentRequest.setModifiedBy(jwtTokenUtil.getUser().getUserId());
                paymentRequestService.update(paymentRequest);
                return transaction;
            }
        } else if (request.getType().isWalletWithdrawal()) {
            if (Objects.isNull(request.getWalletId())) {
                throw new ApplicationException(400, "wallet_required", "Oops! wallet is required to process transaction");
            }
            final var userId = jwtTokenUtil.getUser().getUserId();
            final var wallet = walletService.getWalletByIdAndUserId(request.getWalletId(), userId);
            if (wallet.getAvailableBalance().doubleValue() < request.getAmount().doubleValue()) {
                throw new ApplicationException(400, "insufficient_balance", "debit unsuccessful due to insufficient balance!");
            }
            final var paymentRequestDTO = new CreatePaymentRequestDTO();
            paymentRequestDTO.setId(request.getWalletId());
            paymentRequestDTO.setAmount(request.getAmount());
            paymentRequestDTO.setType(request.getType());
            final var bankAccountDetails = bankAccountDetailsService.getByUserId(userId);
            final var paymentRequest = paymentRequestService.create(paymentRequestDTO);
            paymentRequest.setWallet(wallet);
            return processTransactionThroughProvider(bankAccountDetails, paymentRequest);
        }
        throw new ApplicationException(400, "not_allowed", "Request can't be processed at the moment!");
    }


    private Transaction processTransactionThroughProvider(final BankAccountDetails bankAccountDetails, final PaymentRequest paymentRequest) throws JsonProcessingException {
        return switch (appConfigs.defaultPaymentProcessor) {
            case PAYSTACK -> processAndBuildPayStackTransaction(bankAccountDetails, paymentRequest);
        };
    }

    @Transactional
    public Transaction processAndBuildPayStackTransaction(final BankAccountDetails bankAccountDetails, final PaymentRequest request) throws JsonProcessingException {
        //TODO: Get the unique ID of the transaction before proceeding here
        // check for idempotency so that double debit or credit will be protected and disallowed!
        final var user = jwtTokenUtil.getUser();
        final var transferRequest = getTransferRequestDTO(bankAccountDetails, request);
        final var processorResponse = paymentProcessorService.transfer(transferRequest, bankAccountDetails);
        final var jsonResponse = objectMapper.writeValueAsString(processorResponse);
        final var transaction = new Transaction();
        transaction.setAmount(request.getTotalAmount());
        transaction.setDateCreated(LocalDateTime.now());
        //TODO: This should be updated in the future to use TransactionType
        transaction.setType(OrderType.valueOf(request.getType().name()));
        transaction.setStatus(Status.PENDING);
        transaction.setComment(transferRequest.getReason());
        final var timestamp = System.currentTimeMillis();
        transaction.setReference(String.format("%s_%s_%s", timestamp, bankAccountDetails.getAccountNumber(),
                bankAccountDetails.getReference()));
        transaction.setCreatedBy(user.getUserId());
        transaction.setGateWayMeta(PaymentGatewayMeta.builder().gatewayResponse(jsonResponse).build());
        transaction.setUserId(bankAccountDetails.getUserId());
        transaction.setTenantId(bankAccountDetails.getTenantId());
        if (request.getType().isWalletWithdrawal() && isPayStackValidAmount(processorResponse.getData().getAmount(), transaction.getAmount())) {
            final var walletDebitResponse = walletService.debitWallet(transaction, request.getWallet());
            transaction.setComment("%s was debited successfully from user wallet after processing the payment");
            transaction.setMeta(Map.of("walletDebitResponse", walletDebitResponse));
            transaction.setDateCreated(LocalDateTime.now());
            transaction.setStatus(Status.COMPLETED);
        }
        return transactionService.save(transaction);
    }

    private @NotNull TransferRequestDTO getTransferRequestDTO(final BankAccountDetails bankAccountDetails, final PaymentRequest request) {
        final var event = Objects.nonNull(request.getEventId()) ? request.getEvent() : null;
        final var user = jwtTokenUtil.getUser();
        if (Objects.nonNull(event) && !event.getTenantId().equals(bankAccountDetails.getTenantId()))
            throw new ApplicationException(400, "bad_request", "Recipient is not the owner of the resource!");
        if (Objects.nonNull(event) && !user.getAccountType().isIndividualOrOrganizationMerchantOwner())
            throw new ApplicationException(400, "bad_request", "Oops! Only Owner can request for event settlement payment!");
        return getRequestDTO(bankAccountDetails, request);
    }

    private @NotNull TransferRequestDTO getRequestDTO(final BankAccountDetails bankAccountDetails, final PaymentRequest request) {
        final var transferRequest = new TransferRequestDTO();
        transferRequest.setAmount(request.getTotalAmount());
        transferRequest.setRecipient(bankAccountDetails.getReference());
        transferRequest.setSource(PAYSTACK_SOURCE);
        transferRequest.setReason(String.format("Event %s payment to %s",
                request.getType().name().toLowerCase(),
                bankAccountDetails.getAccountName()));
        transferRequest.setPaymentProcessorType(PaymentProcessorType.PAYSTACK);
        return transferRequest;
    }

    private boolean isPayStackValidAmount(final double paymentProcessedAmount, final BigDecimal transactionAmount) {
        final var isValidAmount = ((new BigDecimal(String.valueOf(paymentProcessedAmount))
                .divide(new BigDecimal(100))).doubleValue() >= transactionAmount.doubleValue());
        return isValidAmount;
    }
}
