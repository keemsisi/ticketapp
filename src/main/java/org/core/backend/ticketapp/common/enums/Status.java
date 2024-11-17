package org.core.backend.ticketapp.common.enums;

public enum Status {
    PAID,
    PENDING,
    ACTIVE,
    SCANNED,
    DELETED,
    BLACKLISTED,
    EXPIRED,
    COMPLETED,
    LESSER_AMOUNT_PAID;

    public boolean isPaid() {
        return this == PAID;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }
}
