package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRecipientResponseDTO {
    private boolean active;
    private String createdAt;
    private String currency;
    private String domain;
    private long id;
    private int integration;
    private String name;
    private String recipientCode;
    private String type;
    private String updatedAt;
    private boolean isDeleted;
    private Details details;

    @Getter
    @Setter
    public static class Details {
        private String authorizationCode; // Nullable
        private String accountNumber;
        private String accountName;
        private String bankCode;
        private String bankName;
    }
}
