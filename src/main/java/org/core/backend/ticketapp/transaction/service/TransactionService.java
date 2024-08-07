package org.core.backend.ticketapp.transaction.service;

import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyResponseDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;

import java.util.List;
import java.util.UUID;


public interface TransactionService {
    List<Transaction> getAll();
    PlanDTO getAllPlans() throws Exception;
    PlanCreateResponseDTO createPlan(PlanCreateRequestDTO createPlanDto) throws Exception;
    TransactionInitializeResponseDTO initializePayment(TransactionInitializeRequestDTO initializePaymentDto);
    TransactionVerifyResponseDTO verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception;
}
