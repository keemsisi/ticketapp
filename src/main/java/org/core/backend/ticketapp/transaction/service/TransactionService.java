package org.core.backend.ticketapp.transaction.service;

import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;


public interface TransactionService {
    String verifyPayment(TransactionVerifyRequestDTO requestDTO);}
