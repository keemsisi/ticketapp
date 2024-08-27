package org.core.backend.ticketapp.subscription.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record SubscriptionCreateResponseDTO(Boolean status, String message, Data data) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
            @JsonProperty("customer")
            int customer,

            @JsonProperty("plan")
            int plan,

            @JsonProperty("integration")
            int integration,

            @JsonProperty("domain")
            String domain,

            @JsonProperty("start")
            Long start,

            @JsonProperty("status")
            String status,

            @JsonProperty("quantity")
            int quantity,

            @JsonProperty("subscription_code")
            String subscriptionCode,

            @JsonProperty("amount")
            BigDecimal amount,

            @JsonProperty("createdAt")
            String createdAt
    ) {}
}
