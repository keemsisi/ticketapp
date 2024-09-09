package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeTransferResponseDTO {
    private boolean status;
    private String message;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String domain;
        private int amount;
        private String currency;
        private String reference;
        private String source;
        private Object sourceDetails; // Using Object since it can be null
        private String reason;
        private String status;
        private Object failures; // Using Object since it can be null
        private String transferCode;
        private Object titanCode; // Using Object since it can be null
        private Object transferredAt; // Using Object since it can be null
        private int id;
        private int integration;
        private int recipient;
        private String createdAt;
        private String updatedAt;
    }
}
