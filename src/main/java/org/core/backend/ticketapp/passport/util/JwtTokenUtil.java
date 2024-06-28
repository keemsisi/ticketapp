package org.core.backend.ticketapp.passport.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@Slf4j
public class JwtTokenUtil {

    //720 hours (30 days)
    public static final long JWT_TOKEN_VALIDITY = 720 * 60 * 60;

    @Autowired
    private ObjectMapper objectMapper;

    public Object getClaimByKey(Object key){
        var attributes = getAllClaims();
        var value = attributes.get(key);
        if(Objects.nonNull(value)) {
            return value;
        }
        throw  new ApplicationException(403, "403", "You don't have the right permission.");
    }

    public Map<String, Object> getAllClaims() {

        Authentication authToken = getAuthentication();
        Map<String, Object> attributes = new HashMap<>();
        if (authToken instanceof OAuth2AuthenticationToken) {
            attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        } else if (authToken instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        }
        return attributes;
    }

    public LoggedInUserDto getUser(){
        return objectMapper.convertValue(getAllClaims(), LoggedInUserDto.class);
    }

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
