package org.core.backend.ticketapp.common.enums;

import java.util.List;

public enum AccountType {
    INDIVIDUAL("individual"),
    INDIVIDUAL_MERCHANT_OWNER("individual_merchant_owner"),
    INDIVIDUAL_MERCHANT_USER("individual_merchant_user"),
    ORGANIZATION_BUYER_OWNER("organization_buyer_owner"),
    ORGANIZATION_BUYER_USER("organization_buyer_user"),
    ORGANIZATION_MERCHANT_OWNER("organization_merchant_owner"),
    ORGANIZATION_MERCHANT_USER("organization_merchant_user"),
    TENANT_USER("tenant_user"),
    SYSTEM_ADMIN_USER("system_admin_user"),
    SUPER_ADMIN("super_admin");
    final String type;

    AccountType(String type) {
        this.type = type;
    }

    public static List<String> getPossibleAminAccountType() {
        return List.of(AccountType.INDIVIDUAL_MERCHANT_OWNER.type,
                AccountType.INDIVIDUAL_MERCHANT_USER.type,
                AccountType.ORGANIZATION_BUYER_OWNER.type,
                AccountType.ORGANIZATION_BUYER_USER.type,
                AccountType.ORGANIZATION_MERCHANT_OWNER.type,
                AccountType.ORGANIZATION_MERCHANT_USER.type
        );
    }

    public static List<String> getAllowedSystemAdminAccountType() {
        return List.of(SUPER_ADMIN.name(), SYSTEM_ADMIN_USER.name());
    }

    public static List<AccountType> allowedForUserOnboarding() {
        return List.of(
                INDIVIDUAL_MERCHANT_OWNER,
                ORGANIZATION_BUYER_OWNER,
                ORGANIZATION_MERCHANT_OWNER,
                INDIVIDUAL
        );
    }

    public String getType() {
        return type;
    }
}
