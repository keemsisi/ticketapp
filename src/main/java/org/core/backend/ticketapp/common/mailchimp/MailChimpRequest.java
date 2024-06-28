package org.core.backend.ticketapp.common.mailchimp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailChimpRequest {
    private String key;
    private SendMessage message;
}
