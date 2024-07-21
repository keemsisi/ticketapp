package org.core.backend.ticketapp.common.enums;

public enum UserType {
    INDIVIDUAL("individual"),
    REGULAR("individual"),
    TENANT_ADMIN("tenant_admin"),
    MERCHANT_OWNER("merchant_owner"),
    MERCHANT_USER("merchant_user"),
    TENANT_USER("tenant_user"),
    SYSTEM_ADMIN_USER("system_admin_user"),
    SUPER_ADMIN("super_admin");
    final String type;

    UserType(String tenantUser) {
        this.type = tenantUser;
    }
}
