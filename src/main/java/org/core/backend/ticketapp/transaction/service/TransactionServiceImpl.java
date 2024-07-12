package org.core.backend.ticketapp.transaction.service;

import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    public String verifyPayment(TransactionVerifyRequestDTO requestDTO) {
        String url = "https://api.paystack.co/transaction/verify/" + requestDTO.reference();
        return "success";
    }
}
