package org.core.backend.ticketapp.transaction.dto.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.transaction.dto.wallet.TransactionChannel;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class TransactionRequestDTO {
    private TransactionChannel channel;
    private String narration;
    @Min(value = 5, message = "Minimum amount required for payment is 5")
    private BigDecimal amount;
}
