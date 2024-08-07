package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.PricingPlanType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionVerifyRequestDTO {
        private UUID userId;
        private String reference;
        private String status;
        private UUID orderId;
        private BigDecimal amount;
        private String gateway;
        private String gatewayResponse;
        private String channel;
        private String currency;
        private String ipAddress;

        @Enumerated(EnumType.STRING)
        private PricingPlanType pricingPlan = PricingPlanType.STANDARD;

        private String paidAt;
}
