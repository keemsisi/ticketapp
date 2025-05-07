package org.core.backend.ticketapp.passport.service.core.mail;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.MailChimpService;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.MailRequest;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.SendMessage;
import org.core.backend.ticketapp.passport.service.core.mail.sendgrid.SendGridService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final AppConfigs appConfigs;
    private final MailChimpService mailChimpService;
    private final SendGridService sendGridService;

    @Override
    public void send(final MailRequest request) {
        switch (appConfigs.emailProvider) {
            case SENDGRID -> sendGridService.send(request);
            case MAILCHIMP -> mailChimpService.send(
                    SendMessage.builder()
                            .subject(request.getSubject())
                            .html(request.getHtml())
                            .to(request.getTo())
                            .build()
            );
        }
    }
}
