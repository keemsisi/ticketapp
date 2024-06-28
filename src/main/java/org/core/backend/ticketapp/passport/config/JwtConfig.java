package org.core.backend.ticketapp.passport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {
    @Autowired
    private KeyPair keyPair;
    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    private String audience;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer}")
    private String issuer;

    @Bean
    public JwtDecoder jwtDecoder() throws GeneralSecurityException {
        var jwtDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();

        OAuth2TokenValidator<Jwt> withExpiry = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> withAudience = new JwtAudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = new JwtIssuerValidator(issuer);

        OAuth2TokenValidator<Jwt> validators = new DelegatingOAuth2TokenValidator<>(withExpiry, withAudience, withIssuer);
        jwtDecoder.setJwtValidator(validators);

        return jwtDecoder;
    }
}
