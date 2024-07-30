package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;
import org.core.backend.ticketapp.common.enums.PricingPlanType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionVerifyResponseDTO {
        @JsonProperty("status")
        private Boolean status;

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private Data data;


        @lombok.Data
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Data {

                @JsonProperty("id")
                private String id;

                @JsonProperty("status")
                private String status;

                @JsonProperty("message")
                private String message;

                @JsonProperty("reference")
                private String reference;

                @JsonProperty("amount")
                private BigDecimal amount;

                @JsonProperty("gateway_response")
                private String gatewayResponse;

                @JsonProperty("receipt_number")
                private String receiptNumber;

                @JsonProperty("domain")
                private String domain;

                @JsonProperty("channel")
                private String channel;

                @JsonProperty("currency")
                private String currency;

                @JsonProperty("ip_address")
                private String ipAddress;

                @JsonProperty("metadata")
                private String metadata;

                @JsonProperty("log")
                private Log log;

                @JsonProperty("fees")
                private int fees;

                @JsonProperty("fees_split")
                private String feesSplit;

                @JsonProperty("authorization")
                private Authorization authorization;

                @JsonProperty("customer")
                private Customer customer;

                @JsonProperty("order_id")
                private String orderId;

                @JsonProperty("split")
                private Split split;

                @JsonProperty("requested_amount")
                private int requestAmount;

                @JsonProperty("pos_transaction_data")
                private String posTransactionData;

                @JsonProperty("source")
                private String source;

                @JsonProperty("connect")
                private String connect;

                @JsonProperty("fees_breakdown")
                private String feesBreakdown;

                @JsonProperty("transaction_date")
                private String transactionDate;

                @JsonProperty("plan_object")
                private PlanObject planObject;

                @JsonProperty("subaccount")
                private SubAccount subAccount;

                @JsonProperty("plan")
                @Enumerated(EnumType.STRING)
                private PricingPlanType pricingPlan;

                @JsonProperty("paidAt")
                private String paidAt;

                @JsonProperty("paid_at")
                private String paid_at;

                @JsonProperty("createdAt")
                private String createdAt;

                @JsonProperty("created_at")
                private String created_at;

                @JsonProperty("payment_date")
                private String paymentDate;

                @JsonProperty("updated_on")
                private Date updatedOn = new Date();

                @lombok.Data
                @NoArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Log {
                        @JsonProperty("success")
                        private Boolean success;

                        @JsonProperty("start_time")
                        private BigInteger startTime;

                        @JsonProperty("time_spent")
                        private int timeSpent;

                        @JsonProperty("attempts")
                        private int attempts;

                        @JsonProperty("errors")
                        private int errors;

                        @JsonProperty("authentication")
                        private String authentication;

                        @JsonProperty("mobile")
                        private Boolean mobile;

                        @JsonProperty("input")
                        private List<?> input;

                        @JsonProperty("history")
                        private List<?> history;
                }

                @lombok.Data
                @NoArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
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
                        private Boolean reusable;

                        @JsonProperty("signature")
                        private String signature;

                        @JsonProperty("account_name")
                        private String accountName;
                }

                @lombok.Data
                @NoArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Customer {
                        @JsonProperty("id")
                        private String id;

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
                        private String metadata;

                        @JsonProperty("risk_action")
                        private String riskAction;

                        @JsonProperty("international_format_phone")
                        private String internationalFormatPhone;
                }

                @lombok.Data
                @NoArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Split {}

                @lombok.Data
                @NoArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class SubAccount {
                }

                @lombok.Data
                @NoArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class PlanObject {}
        }
}
