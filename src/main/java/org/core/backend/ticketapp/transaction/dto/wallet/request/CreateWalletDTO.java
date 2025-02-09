package org.core.backend.ticketapp.transaction.dto.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateWalletDTO {
    private String name;
    @NotNull(message = "userId is required")
    private UUID userId;
}
