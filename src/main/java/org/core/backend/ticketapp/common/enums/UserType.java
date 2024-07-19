package org.core.backend.ticketapp.common.enums;

public enum UserType {
    INDIVIDUAL("individual"),
    MERCHANT_OWNER("merchant_owner"),
    MERCHANT_USER("merchant_user"), ADMIN("admin"),
    SUPER_ADMIN("super_admin");
    final String type;

    UserType(String tenantUser) {
        this.type = tenantUser;
    }
}
