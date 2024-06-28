package org.core.backend.ticketapp.common.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RejectionMessage {
        @JsonProperty("email")
        private String email;
        @JsonProperty("status")
        private String status;
        @JsonProperty("_id")
        private String id;
        @JsonProperty("reject_reason")
        private Object rejectReason;
}