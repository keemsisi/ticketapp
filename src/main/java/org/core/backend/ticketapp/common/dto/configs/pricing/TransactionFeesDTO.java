package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFeesDTO {
    private double tax;
    private double price;
    private double platformFee;
    private double additionalFee;
    private double processingFee;
    private double totalFes;
}
