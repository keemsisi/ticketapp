package org.core.backend.ticketapp.passport.service.core;

import org.core.backend.ticketapp.passport.service.core.mail.EmailProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfigs {
    @Value(value = "${ticket.app.baseUrl}")
    public String baseUrl;
    @Value(value = "${ticket.app.baseUrl.fe}")
    public String baseUrlFe;
    @Value("${system.payment.vendor.paystack.secret.api_key}")
    public String payStackApiKey;
    @Value(value = "${system.default.plan.standard}")
    public UUID defaultPlanId;
    @Value("${user.password.expiration.in.days}")
    public Long passwordExpirationInDays;
    @Value("${system.default.tenant.tenant_id}")
    public UUID defaultTenantId;
    @Value("${server.port}")
    public Integer port;
    @Value("${system.payment.vendor.paystack.callback}")
    public String callback;
    @Value("${system.payment.vendor.paystack.channels}")
    public String[] channels;
    @Value("${system.default.order.max.order}")
    public Long maxOrder;
    @Value("${system.default.payment.processor}")
    public PaymentProcessorType defaultPaymentProcessor;
    @Value("${system.default.email.provider}")
    public EmailProvider emailProvider;
    @Value("${system.default.module.event.id}")
    public UUID eventModuleId;
    @Value("${app.fine-name}")
    public String appName;
    @Value("${ticketapp.password-reset-url}")
    String resetPasswordUrl;
    @Value("${user.failed.login.threshold}")
    Long failedLoginThreshold;
    @Value("${baseFrontEndUrl}")
    String baseFrontEndUrl;
    @Value("${ticketapp.token-secret}")
    String secret;
    @Value("${send-2fa-sms}")
    boolean send2faSms;
    @Value("${system.default.role.onboard_user_role}")
    UUID onboardUserRoleId;
    @Value("${system.default.role.individual_user_role}")
    UUID individualUserRoleId;
    @Value("${system.default.role.tenant_admin_role}")
    UUID tenantAdminRoleId;
    @Value("${system.default.role.tenant_user_role}")
    UUID tenantUserRoleId;
    @Value("${system.default.role.individual_merchant_owner_role}")
    UUID individualMerchantOwnerRole;
    @Value("${system.default.role.individual_merchant_user_role}")
    UUID individualMerchantUserRole;
    @Value("${system.default.role.organization_buyer_owner_role}")
    UUID organizationBuyerOwnerRole;
    @Value("${system.default.role.organization_buyer_user_role}")
    UUID organizationBuyerUserRole;
    @Value("${system.default.role.organization_merchant_owner_role}")
    UUID organizationMerchantOwnerRole;
    @Value("${system.default.role.organization_merchant_user_role}")
    UUID organizationMerchantUserRole;
    @Value("${system.google.api.credential.client_id}")
    public String clientId;
    @Value("${system.google.api.credential.client_secret}")
    public String clientSecret;
    @Value("${system.google.api.credential.refresh_token}")
    public String refreshToken;
    @Value("${system.google.api.credential.auth_code}")
    public String authCode;
}