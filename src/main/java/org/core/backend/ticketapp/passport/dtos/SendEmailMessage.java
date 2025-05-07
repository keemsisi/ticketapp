package org.core.backend.ticketapp.passport.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.To;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailMessage {
    private String html;
    private List<To> tos;
    private String fromName;
    private String subject;
}
