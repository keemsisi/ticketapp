package org.core.backend.ticketapp.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.NotImplementedException;
import org.core.backend.ticketapp.transaction.dto.ApproveSettlementTransferRequestDTO;
import org.core.backend.ticketapp.transaction.dto.SettlementTransferRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;

public interface SettlementService {
    Transaction transfer(final ApproveSettlementTransferRequestDTO request) throws JsonProcessingException;

    default Object processRequest(SettlementTransferRequestDTO request) {
        throw new NotImplementedException();
    }
}
