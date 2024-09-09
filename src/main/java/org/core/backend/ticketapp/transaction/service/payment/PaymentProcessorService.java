package org.core.backend.ticketapp.transaction.service.payment;

import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentRequestDTO;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentResponseDTO;

public interface PaymentProcessorService {
    ProcessorPaymentResponseDTO transfer(ProcessorPaymentRequestDTO request);
}
