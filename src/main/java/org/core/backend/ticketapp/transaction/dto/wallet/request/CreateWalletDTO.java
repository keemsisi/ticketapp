package org.core.backend.ticketapp.transaction.dto.wallet.request;

import lombok.*;

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
}
