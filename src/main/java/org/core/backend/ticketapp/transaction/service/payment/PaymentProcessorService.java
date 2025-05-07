package org.core.backend.ticketapp.transaction.service.payment;

import org.core.backend.ticketapp.transaction.dto.BankResponse;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentRequestDTO;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentResponseDTO;

public interface PaymentProcessorService {
    ProcessorPaymentResponseDTO transfer(ProcessorPaymentRequestDTO request, final BankAccountDetails bankAccountDetails);

    BankResponse getBanks();
}
