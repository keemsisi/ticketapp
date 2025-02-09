package org.core.backend.ticketapp.transaction.dto.wallet.request;

import lombok.*;
import org.core.backend.ticketapp.transaction.entity.wallet.WalletType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateWalletDTO {
    private String name;
    @NotNull(message = "userId is required")
    private UUID userId;
    private WalletType walletType;
    @NotBlank(message = "currency is required")
    private String currency;
}
