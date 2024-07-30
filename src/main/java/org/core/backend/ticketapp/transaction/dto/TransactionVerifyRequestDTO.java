package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.PricingPlanType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionVerifyRequestDTO {
        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "reference")
        private String reference;

        @Column(name = "status")
        private String status;

        @Column(name = "order_id")
        private UUID orderId;
        
        @Column(name = "amount")
        private BigDecimal amount;
        
        @Column(name = "gateway")
        private String gateway;
        
        @Column(name = "gateway_response")
        private String gatewayResponse;
        
        @Column(name = "channel")
        private String channel;
        
        @Column(name = "currency")
        private String currency;
        
        @Column(name = "ip_address")
        private String ipAddress;

        @Column(name = "pricing_plan")
        @Enumerated(EnumType.STRING)
        private PricingPlanType pricingPlan = PricingPlanType.STANDARD;
        
        @Column(name = "paid_at")
        private String paidAt;
}
