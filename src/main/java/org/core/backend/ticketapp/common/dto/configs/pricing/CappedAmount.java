package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class CappedAmount {
    private String processor;
    private String currency;
    private String amount;
    private String percentage;
}