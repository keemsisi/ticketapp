package org.core.backend.ticketapp.transaction.service.payment.paystack;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.transaction.service.payment.PaymentProcessorService;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentRequestDTO;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentResponseDTO;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PayStackPaymentProcessorImpl implements PaymentProcessorService {
    @Override
    public ProcessorPaymentResponseDTO transfer(final ProcessorPaymentRequestDTO request) {
        return null;
    }
}
