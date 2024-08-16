package org.core.backend.ticketapp.transaction.service;

import org.core.backend.ticketapp.transaction.dto.InitTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentInitResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TransactionService {

    Page<Transaction> getAll(Pageable pageable);

    PaymentInitResponseDTO initializePayment(InitTransactionRequestDTO initializePaymentDto);

    boolean verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception;
}
