package org.core.backend.ticketapp.passport.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public class JwtAudienceValidator implements OAuth2TokenValidator<Jwt> {

    private static OAuth2Error INVALID_AUD = new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED,
            "This aud claim does not contain the configured audience",
            "https://tools.ietf.org/html/rfc6750#section-3.1");

    private final String audience;

    public JwtAudienceValidator(String audience) {
        Assert.notNull(audience, "audience cannot be null");
        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        Assert.notNull(jwt, "token cannot be null");

        if (jwt.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        } else {
            return OAuth2TokenValidatorResult.failure(INVALID_AUD);
        }
    }
}
