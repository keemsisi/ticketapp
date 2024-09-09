package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
