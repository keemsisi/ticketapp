package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalFee {
    private String processor;
    private FeeType feeType;
    private String amount;
    private String additionalFeeType;
    private String additionalAmount;
    private String currency;
}
