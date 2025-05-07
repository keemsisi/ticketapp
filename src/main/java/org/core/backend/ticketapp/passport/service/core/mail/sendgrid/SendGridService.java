package org.core.backend.ticketapp.passport.service.core.mail.sendgrid;

import com.sendgrid.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.MailRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class SendGridService {
    public void send(final MailRequest mailRequest) {
        String apiKey = "YOUR_SENDGRID_API_KEY"; // Replace with your SendGrid API key
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            // Set email details
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            // Send the email
            Response response = sg.api(request);
            log.info(">>> SendGrid Email Resppnse : {} ", response);
        } catch (IOException ex) {
            log.error("Error sending email: " + ex);
        }
    }
}
