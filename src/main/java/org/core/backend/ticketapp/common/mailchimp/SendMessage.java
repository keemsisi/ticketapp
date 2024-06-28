package org.core.backend.ticketapp.common.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SendMessage {
    private String text;
    private String subject;
    private String html;
    @JsonProperty("from_email")
    private String fromEmail;
    @JsonProperty("from_name")
    private String fromName;
    private List<To> to = null;
}