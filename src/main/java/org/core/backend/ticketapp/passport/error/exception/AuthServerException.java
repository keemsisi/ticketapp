package org.core.backend.ticketapp.passport.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class AuthServerException implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception exception) throws Exception {
        if(exception instanceof ApplicationException){
            ApplicationException ex = (ApplicationException) exception;
            return ResponseEntity
                    .status(ex.getHttpStatus())
                    .body(new CustomOauthException(ex.getHttpStatus(), ex.getCode(), ex.getMessage()));
        }
        else if (exception instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new CustomOauthException(oAuth2Exception.getMessage()));
        }else if(exception instanceof AuthenticationException){
            AuthenticationException authenticationException = (AuthenticationException) exception;
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CustomOauthException(authenticationException.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CustomOauthException(exception.getMessage()));
    }
}