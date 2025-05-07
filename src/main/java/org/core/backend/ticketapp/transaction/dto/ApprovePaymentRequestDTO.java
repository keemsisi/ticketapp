package org.core.backend.ticketapp.transaction.dto;

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
public class ApprovePaymentRequestDTO {
    private UUID paymentRequestId;
    @NotNull(message = "paymentRequestType can't be null")
    private PaymentRequestType type;
    //wallet withdrawal request will require this
    private BigDecimal amount;
    private UUID walletId;
}
