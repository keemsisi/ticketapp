package org.core.backend.ticketapp.transaction.dto.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionRequestDTO extends TransactionRequestDTO {
    @NotNull(message = "senderWalletId can't be blank")
    private UUID senderWalletId;
    @NotNull(message = "receiverWalletId can't be blank")
    private UUID receiverWalletId;
}
