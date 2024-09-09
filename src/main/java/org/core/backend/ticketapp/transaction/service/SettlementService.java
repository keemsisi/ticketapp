package org.core.backend.ticketapp.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.transaction.dto.TransferRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;

public interface SettlementService {
    Transaction transfer(final TransferRequestDTO request) throws JsonProcessingException;
}
