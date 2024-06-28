package org.core.backend.ticketapp.passport.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.passport.config.AuthenticationProviderConfig;
import org.core.backend.ticketapp.passport.repository.UserRepository;
import org.core.backend.ticketapp.passport.service.RedisService;
import org.core.backend.ticketapp.passport.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    private static final String AUTH_2FA = "_AUTH_2FA";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SmsService smsService;
    @Autowired
    private AuthenticationProviderConfig authenticationProviderConfig;
    @Autowired
    private AuthenticationManager authenticationManager;


}
