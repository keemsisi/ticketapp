package org.core.backend.ticketapp.transaction.service.payment.dto;

import org.core.backend.ticketapp.passport.service.core.PaymentProcessorType;

public interface ProcessorPaymentRequestDTO {
    PaymentProcessorType getPaymentProcessorType();
}
