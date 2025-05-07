package org.core.backend.ticketapp.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.transaction.dto.ApprovePaymentRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;

public interface SettlementService {
    Transaction processApprovedTransfer(final ApprovePaymentRequestDTO request) throws JsonProcessingException;

}
