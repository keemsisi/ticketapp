package org.core.backend.ticketapp.common.enums;

public enum SubscriptionStatus {
    ACTIVE, IN_ACTIVE, DISABLED, SUSPENDED;

    public boolean isActive(){
        return this == ACTIVE;
    }
}
