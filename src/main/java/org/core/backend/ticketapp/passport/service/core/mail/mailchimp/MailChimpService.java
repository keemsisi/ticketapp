package org.core.backend.ticketapp.passport.service.core.mail.mailchimp;

import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.MailChimpRequest;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.MailChimpResponse;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
public class MailChimpService {

    private static final Logger log = LoggerFactory.getLogger(MailChimpService.class);
    @Value("${mailchimp.key}")
    private String key;
    @Value("${mailchimp.base-url}")
    private String baseUrl;
    @Value("${mailchimp.from-email}")
    private String fromEmail;
    @Value("${mailchimp.from-name}")
    private String fromName;
    @Autowired
    private RestTemplate restTemplate;

    public MailChimpResponse[] send(SendMessage sendMessage) {
        sendMessage.setFromEmail(fromEmail);
        sendMessage.setFromName(fromName);
        var request = MailChimpRequest.builder()
                .message(sendMessage)
                .key(key)
                .build();
        var uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .build()
                .toString();
        var httpEntity = new HttpEntity<>(request);
        final MailChimpResponse[] response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, MailChimpResponse[].class).getBody();
        log.info(">>>> Mailchimp Email response : {}", Arrays.toString(response));
        return response;
    }
}
