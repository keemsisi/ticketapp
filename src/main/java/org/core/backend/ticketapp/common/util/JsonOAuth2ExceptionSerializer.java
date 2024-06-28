package org.core.backend.ticketapp.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.OAuth2ExceptionJackson2Serializer;

import java.io.IOException;
import java.util.Map;

@Configuration
public class JsonOAuth2ExceptionSerializer extends OAuth2ExceptionJackson2Serializer {

    @Override
    public void serialize(OAuth2Exception value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        String reason;
        Throwable cause = value.getCause();
        if (cause instanceof CredentialsExpiredException) {
            reason = "credentials_expired";
        } else if (cause instanceof DisabledException) {
            reason = "account_disabled";
        } else if (cause instanceof AccountExpiredException) {
            reason = "account_expired";
        } else if (cause instanceof LockedException) {
            reason = "account_locked";
//        } else if (cause instanceof FirstLoginException) {
//            reason = PassportError.FIRST_LOGIN.code();
//        } else if (cause instanceof NotVerifiedException) {
//            reason = ((NotVerifiedException) cause).getErrorCode();
        } else {
            reason = null;
        }

        jgen.writeStartObject();
        jgen.writeStringField("error", value.getOAuth2ErrorCode());
        jgen.writeStringField("error_description", value.getMessage());
        jgen.writeStringField("error_code", value.getOAuth2ErrorCode());
        jgen.writeStringField("code", value.getOAuth2ErrorCode());
        jgen.writeStringField("description", value.getMessage());
        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jgen.writeStringField(key, add);
            }
        }
        // error reasons for invalid grant related to account status
        if (StringUtils.isNotBlank(reason)) {
            jgen.writeStringField("error_reason", reason);
        }
        jgen.writeEndObject();
    }
}
