package org.core.backend.ticketapp.passport.service;

import org.core.backend.ticketapp.common.mailchimp.MailChimpRequest;
import org.core.backend.ticketapp.common.mailchimp.MailChimpResponse;
import org.core.backend.ticketapp.common.mailchimp.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MailChimpService {

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
        var httpEntity = new HttpEntity(request);
        return restTemplate.exchange(uri, HttpMethod.POST, httpEntity, MailChimpResponse[].class).getBody();
    }
}
