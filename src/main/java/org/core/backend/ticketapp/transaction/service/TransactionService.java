package org.core.backend.ticketapp.transaction.service;

import org.core.backend.ticketapp.transaction.dto.InitPaymentOrderRequestDTO;
import org.core.backend.ticketapp.transaction.dto.OrderResponseDto;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.dto.SettlementRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TransactionService {

    Page<Transaction> getAll(Pageable pageable);

    OrderResponseDto initializePayment(InitPaymentOrderRequestDTO initializePaymentDto);

    Transaction verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception;

    Transaction transfer(SettlementRequestDTO request);

    Transaction save(Transaction transaction);
}
