package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRecipientRequestDTO {
    private String type;
    private String name;
    private String accountNumber;
    private String bankCode;
    private String currency;
}
