package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRecipientRequestDTO {
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("bank_code")
    private String bankCode;
    @JsonProperty("currency")
    private String currency;
}
