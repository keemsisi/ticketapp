package org.core.backend.ticketapp.transaction.dto.wallet.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionResponseDTO extends TransactionResponseDTO {
    private WalletTransactionOwnerDTO sender;
    private WalletTransactionOwnerDTO receiver;
}
