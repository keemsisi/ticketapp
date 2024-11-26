package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionProcessingFeesDTO {
    private double price;
    private FeeDTO fee;
    private TaxDTO tax;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeDTO {
        private FeeType type;
        private double value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxDTO {
        private FeeType type;
        private double value;
    }
}
