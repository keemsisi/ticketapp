package org.core.backend.ticketapp.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.common.dto.configs.pricing.TransactionFeesDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.passport.service.core.PaymentProcessorType;
import org.core.backend.ticketapp.transaction.dto.*;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;


public interface TransactionService {

    Page<Transaction> getAll(Pageable pageable);

    OrderResponseDto initializePayment(InitPaymentOrderRequestDTO initializePaymentDto);

    Transaction verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception;

    Transaction transfer(ApprovePaymentRequestDTO request);

    Transaction save(Transaction transaction);

    void processPaystackWebhook(PaystackWebhookEvent request) throws JsonProcessingException;

    Transaction getById(UUID transactionId);

    TransactionFeesDTO getTransactionFees(Event event, double amount, PaymentProcessorType type, String currency);

    TransactionFeesDTO getTransactionFees(UUID eventSeatSectionId, PaymentProcessorType type, String currency);
}
