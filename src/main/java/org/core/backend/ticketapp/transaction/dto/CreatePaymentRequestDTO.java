package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.transaction.entity.request.PaymentRequestType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequestDTO {
    @NotNull(message = "id can't be null")
    @JsonAlias({"eventId", "walletId"})
    private UUID id; // could be wallet_id or event_id
    @NotNull(message = "amount can't be null")
    private BigDecimal amount;
    private PaymentRequestType type = PaymentRequestType.EVENT_SETTLEMENT;
}
