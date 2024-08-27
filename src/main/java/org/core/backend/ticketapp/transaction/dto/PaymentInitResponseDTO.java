package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitResponseDTO {

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Data data;

    @Getter
    @Setter
    public static class Data {

        @JsonProperty("authorization_url")
        private String authorizationUrl;

        @JsonProperty("access_code")
        private String accessCode;

        @JsonProperty("reference")
        private String reference;
    }
}
