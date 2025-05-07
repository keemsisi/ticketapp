package org.core.backend.ticketapp.transaction.dto.wallet.response;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionOwnerDTO {
    private Long walletId;
    private String walletName;
    private String accountNumber;
}
