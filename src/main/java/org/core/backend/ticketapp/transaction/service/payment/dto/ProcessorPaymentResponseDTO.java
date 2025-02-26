package org.core.backend.ticketapp.transaction.service.payment.dto;

import org.core.backend.ticketapp.transaction.service.payment.paystack.dto.TransferResponseDTO;

public interface ProcessorPaymentResponseDTO {
    default boolean isStatus() {
        throw new UnsupportedOperationException("Method is not supported at the moment");
    }

    default String getMessage() {
        throw new UnsupportedOperationException("Method is not supported at the moment");
    }

    default TransferResponseDTO.Data getData() {
        throw new UnsupportedOperationException("Method is not supported at the moment");
    }
}
