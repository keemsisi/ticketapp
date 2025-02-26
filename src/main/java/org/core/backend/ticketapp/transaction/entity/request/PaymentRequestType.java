package org.core.backend.ticketapp.transaction.entity.request;

public enum PaymentRequestType {
    EVENT_SETTLEMENT,
    WALLET_WITHDRAWAL;

    public boolean isWalletWithdrawal() {
        return this == WALLET_WITHDRAWAL;
    }

    public boolean isEventSettlement() {
        return this == EVENT_SETTLEMENT;
    }
}
