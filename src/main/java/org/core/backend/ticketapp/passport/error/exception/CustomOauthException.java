package org.core.backend.ticketapp.passport.error.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {
    public int STATUS = 401;
    public String CODE = "invalid_request";
    public String MESSAGE = "Invalid username or password";

    public CustomOauthException(String msg) {

        super(msg);
    }

    public CustomOauthException(int status, String code, String msg) {
        super(msg);
        this.STATUS = status;
        this.CODE = code;
        this.MESSAGE = msg;
    }

    @Override
    public String getOAuth2ErrorCode() {
        return CODE;
    }

}
