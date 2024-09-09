package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentRequestDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDTO implements ProcessorPaymentRequestDTO {
    private boolean status;
    private String message;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private int integration;
        private String domain;
        private int amount;
        private String currency;
        private String source;
        private String reason;
        private int recipient;
        private String status;
        private String transferCode;
        private int id;
        private String createdAt;
        private String updatedAt;
    }
}
