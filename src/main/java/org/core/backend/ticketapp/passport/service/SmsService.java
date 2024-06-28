package org.core.backend.ticketapp.passport.service;

import org.core.backend.ticketapp.common.properties.SmsProperties;
import org.core.backend.ticketapp.common.request.MessagingServiceRequest;
import org.core.backend.ticketapp.common.request.SmsRequest;
import org.core.backend.ticketapp.common.response.SmsResponse;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class SmsService {
    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final SmsProperties properties;
    @Value(value = "${messaging-service.single-sms}")
    private String singleSmsBaseUrl;
    @Value(value = "${messaging-service.apiKey}")
    private String smsApiKey;
    @Value(value = "${messaging-service.from}")
    private String messagingServiceFrom;

    public SmsService(RestTemplate restTemplate, SmsProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public SmsResponse sendSms(String message, String to) {
        var request = new SmsRequest();
        BeanUtils.copyProperties(properties, request);
        request.setSms(message);
        request.setTo(UserUtils.normalizePhoneNumber(to));
        return restTemplate.postForObject(properties.getUrl(), request, SmsResponse.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> sendSingleSms(MessagingServiceRequest message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", smsApiKey);
        message.setFrom(messagingServiceFrom);
        HttpEntity<MessagingServiceRequest> entity = new HttpEntity<>(message, headers);
        return restTemplate.postForObject(singleSmsBaseUrl, entity, Map.class);
    }
}
