package org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailChimpRequest {
    private String key;
    private SendMessage message;
}
