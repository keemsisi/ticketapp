package org.core.backend.ticketapp.transaction.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class PaystackWebhookEvent {
    private String event;
    private Data data;


    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    public static class Data {
        private final String createdAt;
        private String domain;
        private String status;
        private String subscriptionCode;
        private int amount;
        private String cronExpression;
        private String nextPaymentDate;
        private String openInvoice;
        private Plan plan;
        private Authorization authorization;
        private Customer customer;
    }


    @lombok.Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    public static class Plan {
        private String name;
        private String planCode;
        private String description;
        private int amount;
        private String interval;
        private boolean sendInvoices;
        private boolean sendSms;
        private String currency;
    }

    @lombok.Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    public static class Authorization {
        private String authorizationCode;
        private String bin;
        private String last4;
        private String expMonth;
        private String expYear;
        private String cardType;
        private String bank;
        private String countryCode;
        private String brand;
        private String accountName;
    }

    @lombok.Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    public static class Customer {
        private String firstName;
        private String lastName;
        private String email;
        private String customerCode;
        private String phone;
        private Map<String, String> metadata;
        private String riskAction;
    }
}
