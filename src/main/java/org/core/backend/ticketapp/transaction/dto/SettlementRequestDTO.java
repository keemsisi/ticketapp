package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.TransactionType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementRequestDTO {
    @NotNull(message = "eventId can't be null")
    private UUID eventId;
    @NotNull(message = "userId can't be null")
    private UUID userId;
    @NotNull(message = "amount can't be null")
    private BigDecimal amount;
    private TransactionType transactionType = TransactionType.SETTLEMENT;
}
