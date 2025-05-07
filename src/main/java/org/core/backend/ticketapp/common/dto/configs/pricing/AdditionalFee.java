package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalFee {
    private String processor;
    private FeeType type;
    private String amount;
    private String additionalAmount;
    private String currency;
}
