package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {
    private UUID eventId;
    private UUID userId;
    private BigDecimal amount;
}
