package org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto;

import lombok.Data;

import java.util.List;

@Data
public class MailRequest {
    private String text;
    private String subject;
    private String html;
    private String fromEmail;
    private String fromName;
    private List<To> to = null;
}