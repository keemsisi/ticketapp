package org.core.backend.ticketapp.transaction.dto.wallet.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.Status;
import org.core.backend.ticketapp.transaction.dto.wallet.TransactionChannel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private BigDecimal fee;
    private BigDecimal amount;
    private String reference;
    private LocalDateTime dateCreated;
    private LocalDateTime dateCompleted;
    private Status status;
    private String description;
    private TransactionChannel channel;
    private Long transactionId;
}
