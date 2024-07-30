package org.core.backend.ticketapp.plan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanCreateResponseDTO(
        Boolean status,
        String message,
        Data data
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
            @JsonProperty("name")
            String name,

            @JsonProperty("amount")
            String amount,

            @JsonProperty("interval")
            String interval,

            @JsonProperty("integration")
            String integration,

            @JsonProperty("plan_code")
            String planCode,

            @JsonProperty("send_invoices")
            String sendInvoices,

            @JsonProperty("send_sms")
            String sendSms,

            @JsonProperty("currency")
            String currency,

            @JsonProperty("id")
            String id,

            @JsonProperty("createdAt")
            String createdAt,

            @JsonProperty("updatedAt")
            String updatedAt
    ) {}
}
