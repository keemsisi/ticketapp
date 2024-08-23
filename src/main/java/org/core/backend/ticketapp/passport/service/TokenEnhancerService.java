package org.core.backend.ticketapp.passport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.core.backend.ticketapp.passport.dtos.core.BasicClientDetails;
import org.core.backend.ticketapp.passport.entity.Tenant;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("deprecation")
public class TokenEnhancerService implements TokenEnhancer {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer}")
    private String issuer;
    @Value("${user.activity.idle.time}")
    private Long userIdleTime;

    @Autowired
    private ClientService clientService;
    @Autowired
    private CoreUserService coreUserService;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TenantService tenantService;

    @SneakyThrows
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInformation = new HashMap<>();
        User user = null;
        if (!authentication.isClientOnly()) {
            user = (User) authentication.getPrincipal();
            final var tenantId = user.getTenantId();
            final var tenant = Objects.isNull(tenantId) ? new Tenant() : tenantService.getByTenantId(user.getTenantId()).orElse(new Tenant());
            var permissions = coreUserService.getUserPermissions(user.getId()).orElseThrow();
            additionalInformation.put("first_name", user.getFirstName());
            additionalInformation.put("last_name", user.getLastName());
            additionalInformation.put("email", user.getEmail());
            additionalInformation.put("user_id", user.getId());
            additionalInformation.put("tenant_id", user.getTenantId());
            additionalInformation.put("idle_time", userIdleTime);
            additionalInformation.put("account_type", user.getAccountType());
            additionalInformation.put("user_type", user.getUserType());
            additionalInformation.put("plan_id", tenant.getPlanId());
            additionalInformation.put("exp", Instant.now().getEpochSecond() +
                    TimeUnit.SECONDS.convert(24 * 60, TimeUnit.MINUTES));

            if (!permissions.getActions().isEmpty()) {
                additionalInformation.put("scope", permissions.getActions());
            }

            if (!permissions.getRoles().isEmpty()) {
                additionalInformation.put("roles", permissions.getRoles());
            }

            if (!permissions.getGroups().isEmpty()) {
                additionalInformation.put("groups", permissions.getGroups());
            }

            if (user.isFirstTimeLogin()) {
                additionalInformation.put("scope", List.of("first_time_login"));
            }
        } else {
            String clientId = authentication.getOAuth2Request().getClientId();
            BasicClientDetails client = clientService.getBasicClientDetails(clientId);
            additionalInformation.put("client_name", client.getClientName());
            if (!client.getDomains().isEmpty()) {
                additionalInformation.put("domains", client.getDomains());
            }
        }
        additionalInformation.put("iss", issuer);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        activityLogProcessorUtils.processActivityLog(user != null ? user.getId() : null, User.class.getTypeName(), null, objectMapper.writeValueAsString(additionalInformation), "User login");
        return accessToken;
    }
}

