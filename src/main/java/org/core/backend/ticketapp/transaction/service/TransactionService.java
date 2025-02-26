package org.core.backend.ticketapp.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.transaction.dto.*;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface TransactionService {

    Page<Transaction> getAll(Pageable pageable);

    OrderResponseDto initializePayment(InitPaymentOrderRequestDTO initializePaymentDto);

    Transaction verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception;

    Transaction transfer(ApprovePaymentRequestDTO request);

    Transaction save(Transaction transaction);

    void processPaystackWebhook(PaystackWebhookEvent request) throws JsonProcessingException;

    Transaction getById(UUID transactionId);
}
