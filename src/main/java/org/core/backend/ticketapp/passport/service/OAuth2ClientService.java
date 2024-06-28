package org.core.backend.ticketapp.passport.service;

import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.security.PrincipalNameAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OAuth2ClientService {

    @Autowired
    private OAuth2AuthorizedClientManager clientManager;
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    public String getBearerAccessToken(String clientName) {
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(clientName);

        var authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(registration.getRegistrationId())
                .principal(new PrincipalNameAuthentication(registration.getClientId()))
                .build();
        OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);
        if (Objects.isNull(client)) {
            throw new ApplicationException(500, "server_error", "Remote service cannot be reached.");
        }
        return client.getAccessToken().getTokenValue();
    }
}
