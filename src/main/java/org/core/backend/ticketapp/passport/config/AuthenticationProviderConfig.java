package org.core.backend.ticketapp.passport.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.request.TwoFaValidationDTO;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.RedisService;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;
import java.util.Objects;

@Configuration
@Slf4j
public class AuthenticationProviderConfig implements AuthenticationProvider {

    private static final String AUTH_2FA = "_AUTH_2FA";
    private static final String DEFAULT_EXT = "+234";
    @Autowired
    private CoreUserService userService;
    @Autowired
    private RedisService redisService;

    @SneakyThrows(value = JsonProcessingException.class)
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String twoFaToken = (String) ((Map<?, ?>) (authentication.getDetails())).get("twoFaToken");
        String username = authentication.getName();
        String password = Objects.nonNull(authentication.getCredentials()) ?
                authentication.getCredentials().toString() : null;
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new ApplicationException(401, "bad_credentials", "invalid username or password");
        }
        var user = (User) userService.loadUserByUsername(username);
        userService.processUserLoginValidation(user);
        var loginStatus = userService.authenticateUser(user, password);
        if (loginStatus) {
            String phone = user.getPhone();
            if (ObjectUtils.isNotEmpty(phone) && !phone.contains("+"))
                phone = DEFAULT_EXT + phone.substring(1);
            if (user.isTwoFaEnabled() && ObjectUtils.isNotEmpty(twoFaToken)) {
                userService.validate2FAToken(TwoFaValidationDTO.builder().userId(user.getId()).otp(twoFaToken).build());
            } else if (user.isTwoFaEnabled() && ObjectUtils.isEmpty(phone)) {
                throw new ApplicationException(400, "missing_phone_number", "Oops! We are unable to send you the 2FA code... Please update your phone number");
            } else if (user.isTwoFaEnabled()) {
                userService.send2FAToken(user.getId() + "", phone, user);
                throw new ApplicationException(200, "2fa_required",
                        String.format("Use the verification code sent to %s, for your TicketApp authentication.",
                                CoreUserService.maskPhoneNumber(phone)));
            }
            userService.updateLogin(user);
            return new UsernamePasswordAuthenticationToken(user, password, null);
        }
        userService.updateFailedLogin(user);
        throw new ApplicationException(401, "bad_credentials", String.format("invalid username or password, just %d attempts left.", user.getAccountLockoutThresholdCount() - user.getLoginAttempt() + 1));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}