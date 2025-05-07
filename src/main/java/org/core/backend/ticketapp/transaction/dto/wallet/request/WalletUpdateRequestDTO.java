package org.core.backend.ticketapp.transaction.dto.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletUpdateRequestDTO {
    private String accountName;
    @NotNull
    private UUID walletId;
    private BigDecimal balance;
    private BigDecimal balanceBefore;
}
