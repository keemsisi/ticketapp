package org.core.backend.ticketapp.common.enums;

public enum UserType {
    SELLER, BUYER;

    public boolean isBuyerOrSeller() {
        return this == SELLER || this == BUYER;
    }

    public boolean isBuyer() {
        return this == BUYER;
    }

    public boolean isSeller() {
        return this == SELLER;
    }


}
