package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionInitializeResponseDTO {
        @JsonProperty("status")
        private Boolean status;

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private Data data;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Data {
                @JsonProperty("authorization_url")
                private String authorizationUrl;

                @JsonProperty("access_code")
                private String accessCode;

                @JsonProperty("reference")
                private String reference;
        }
}
