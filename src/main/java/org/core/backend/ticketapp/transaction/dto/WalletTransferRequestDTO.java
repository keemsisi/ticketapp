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
public class WalletTransferRequestDTO {
    @NotNull(message = "amount can't be null")
    private BigDecimal amount;
    @NotNull(message = "wallet id can't be null")
    private UUID walletId;
    private TransactionType transactionType = TransactionType.WALLET_WITHDRAWAL;
}
