package org.core.backend.ticketapp.transaction.service;

import org.core.backend.ticketapp.transaction.dto.TransactionInitializeRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyResponseDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;

import java.util.List;


public interface TransactionService {
    List<Transaction> getAll();
    TransactionInitializeResponseDTO initializePayment(TransactionInitializeRequestDTO initializePaymentDto);
    TransactionVerifyResponseDTO verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception;
}
