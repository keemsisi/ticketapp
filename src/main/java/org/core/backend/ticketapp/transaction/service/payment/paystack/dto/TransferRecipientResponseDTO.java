package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRecipientResponseDTO {
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Object data;
    @JsonProperty("meta")
    private Meta meta;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Details {
        @JsonProperty("authorization_code")
        private String authorizationCode;
        @JsonProperty("account_number")
        private String accountNumber;
        @JsonProperty("account_name")
        private String accountName;
        @JsonProperty("bank_code")
        private String bankCode;
        @JsonProperty("bank_name")
        private String bankName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Recipient {
        @JsonProperty("active")
        private Boolean active;
        @JsonProperty("createdAt")
        private LocalDateTime createdAt;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("description")
        private String description;
        @JsonProperty("domain")
        private String domain;
        @JsonProperty("email")
        private String email;
        @JsonProperty("id")
        private Long id;
        @JsonProperty("integration")
        private Long integration;
        @JsonProperty("metadata")
        private String metadata;
        @JsonProperty("name")
        private String name;
        @JsonProperty("recipient_code")
        private String recipientCode;
        @JsonProperty("type")
        private String type;
        @JsonProperty("updatedAt")
        private String updatedAt;
        @JsonProperty("is_deleted")
        private Boolean isDeleted;
        @JsonProperty("isDeleted")
        private boolean isDeletedAlias; // Handles both variations in the JSON
        @JsonProperty("details")
        private Details details;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        @JsonProperty("total")
        private int total;
        @JsonProperty("skipped")
        private int skipped;
        @JsonProperty("perPage")
        private int perPage;
        @JsonProperty("page")
        private int page;
        @JsonProperty("pageCount")
        private int pageCount;
    }
}
