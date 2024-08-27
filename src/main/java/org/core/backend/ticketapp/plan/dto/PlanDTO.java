package org.core.backend.ticketapp.plan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanDTO {
    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private List<Data> data;


    @lombok.Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

//        @JsonProperty("subscriptions")
//        private List<Map<String, ?>> subscriptions;

        @JsonProperty("id")
        private String id;

        @JsonProperty("integration")
        private Integer integration;

        @JsonProperty("domain")
        private String domain;

        @JsonProperty("name")
        private String name;

        @JsonProperty("plan_code")
        private String plan_code;

        @JsonProperty("description")
        private String description;

        @JsonProperty("amount")
        private BigDecimal amount;

        @JsonProperty("interval")
        private String interval;

        @JsonProperty("send_invoices")
        private Boolean send_invoices;

        @JsonProperty("send_sms")
        private Boolean sendSms;

        @JsonProperty("hosted_page")
        private Boolean hosted_page;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("createAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;
    }
}
