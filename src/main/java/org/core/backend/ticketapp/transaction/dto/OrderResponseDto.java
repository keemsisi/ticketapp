package org.core.backend.ticketapp.transaction.dto;

import lombok.*;
import org.core.backend.ticketapp.common.dto.configs.pricing.TransactionFeesDTO;
import org.core.backend.ticketapp.order.entity.Order;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Order primary;
    private List<Order> secondary;
    private TransactionFeesDTO transactionFees;
}
