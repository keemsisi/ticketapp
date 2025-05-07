package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentVerificationResponseDTO {
    @JsonProperty("status")
    private boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Data data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {

        @JsonProperty("id")
        private long id;

        @JsonProperty("domain")
        private String domain;

        @JsonProperty("status")
        private String status;

        @JsonProperty("reference")
        private String reference;

        @JsonProperty("amount")
        private long amount;

        @JsonProperty("message")
        private String message;

        @JsonProperty("gateway_response")
        private String gatewayResponse;

        @JsonProperty("paid_at")
        private String paidAt;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("channel")
        private String channel;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("ip_address")
        private String ipAddress;

        @JsonProperty("metadata")
        private Object metadata;

        @JsonProperty("log")
        private Log log;

        @JsonProperty("fees")
        private long fees;

        @JsonProperty("fees_split")
        private String feesSplit;

        @JsonProperty("authorization")
        private Authorization authorization;

        @JsonProperty("customer")
        private Customer customer;

        @JsonProperty("plan")
        private String plan;

        @JsonProperty("order_id")
        private String orderId;

        @JsonProperty("requested_amount")
        private long requestedAmount;

        @JsonProperty("pos_transaction_data")
        private String posTransactionData;

        @JsonProperty("source")
        private String source;

        @JsonProperty("fees_breakdown")
        private String feesBreakdown;

        @JsonProperty("transaction_date")
        private String transactionDate;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Log {

            @JsonProperty("start_time")
            private long startTime;

            @JsonProperty("time_spent")
            private int timeSpent;

            @JsonProperty("attempts")
            private int attempts;

            @JsonProperty("errors")
            private int errors;

            @JsonProperty("success")
            private boolean success;

            @JsonProperty("mobile")
            private boolean mobile;

            @JsonProperty("input")
            private String[] input;

            @JsonProperty("history")
            private History[] history;

            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            public static class History {

                @JsonProperty("type")
                private String type;

                @JsonProperty("message")
                private String message;

                @JsonProperty("time")
                private int time;
            }
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Authorization {

            @JsonProperty("authorization_code")
            private String authorizationCode;

            @JsonProperty("bin")
            private String bin;

            @JsonProperty("last4")
            private String last4;

            @JsonProperty("exp_month")
            private String expMonth;

            @JsonProperty("exp_year")
            private String expYear;

            @JsonProperty("channel")
            private String channel;

            @JsonProperty("card_type")
            private String cardType;

            @JsonProperty("bank")
            private String bank;

            @JsonProperty("country_code")
            private String countryCode;

            @JsonProperty("brand")
            private String brand;

            @JsonProperty("reusable")
            private boolean reusable;

            @JsonProperty("signature")
            private String signature;

            @JsonProperty("account_name")
            private String accountName;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Customer {

            @JsonProperty("id")
            private long id;

            @JsonProperty("first_name")
            private String firstName;

            @JsonProperty("last_name")
            private String lastName;

            @JsonProperty("email")
            private String email;

            @JsonProperty("customer_code")
            private String customerCode;

            @JsonProperty("phone")
            private String phone;

            @JsonProperty("metadata")
            private Object metadagta;

            @JsonProperty("risk_action")
            private String riskAction;

            @JsonProperty("international_format_phone")
            private String internationalFormatPhone;
        }
    }
}

