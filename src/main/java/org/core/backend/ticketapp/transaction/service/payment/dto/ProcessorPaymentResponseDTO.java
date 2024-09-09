package org.core.backend.ticketapp.transaction.service.payment.dto;

public interface ProcessorPaymentResponseDTO {
    default boolean isStatus() {
        throw new UnsupportedOperationException("Method is not supported at the moment");
    }

    default String getMessage() {
        throw new UnsupportedOperationException("Method is not supported at the moment");
    }

    default Object getData() {
        throw new UnsupportedOperationException("Method is not supported at the moment");
    }
}
