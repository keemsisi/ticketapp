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
    FAILED,
    INVALID_AMOUNT_PAID,
    BLOCKED,
    PND,
    FRAUDULENT;

    public boolean isPaid() {
        return this == PAID;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }
}
