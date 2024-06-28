package org.core.backend.ticketapp.passport.service;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class OAuth2RequestExtension extends OAuth2Request {
    private Map<String,  Serializable> additionalDetails;

    @Override
    public Map<String, Serializable> getExtensions() {
        return this.additionalDetails;
    }
}
