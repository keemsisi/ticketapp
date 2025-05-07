package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFeesDTO {
    private double tax;
    private double price;
    private double totalCost; // FEEs  + PRICE
    private double totalFees;
    private double platformFee;
    private double additionalFee;
    private double processingFee;
    private double priceDiscount;
}
