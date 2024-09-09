package org.core.backend.ticketapp.common.enums;

public enum AccountNumberType {
    NUBAN("nuban"),
    GHIPSS("ghipss"),
    MOBILE_MONEY("mobile_money"),
    BASA("basa");
    private final String type;


    AccountNumberType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
