package org.core.backend.ticketapp.transaction.common;

import lombok.Getter;

@Getter
public enum ResponseCodeMapping {
    OK(20000, "Request was successful"),
    NOT_FOUND(20001, "Resource not found!"),
    BAD_REQUEST(20002, "Oops! Bad Request!"),

    BANK_LOOKUP_OK(30000, "Bank lookup was successful"),
    BANK_TRANSFER_OK(30001, "Bank transfer was successful"),
    BANK_TRANSFER_INIT_OK(30002, "Bank transfer initialization was successful!"),

    TRANSACTION_SUCCESSFUL(40000, "Transaction was successful"),
    TRANSACTION_FAILED(40001, "Transaction failed to process"),
    TRANSACTION_INVALID(40002, "Transaction experienced invalid params"),

    WALLET_TRANSACTION_CREDIT_OK(50000, "Transaction experienced invalid params"),
    WALLET_TRANSACTION_CREDIT_FAILED(50001, "Wallet transaction credit failed"),
    WALLET_TRANSACTION_DEBIT_FAILED(50002, "Wallet transaction debit failed"),
    WALLET_TRANSACTION_INSUFFICIENT_BALANCE(50003, "Wallet transaction failed due to insufficient balance"),
    WALLET_TRANSACTION_INIT_OK(60004, "Wallet transaction init successful!"),

    CARD_TRANSACTION_CREDIT_OK(60000, "Card transaction was successful"),
    CARD_TRANSACTION_CREDIT_FAILED(60001, "Oops! Failed to credit wallet!"),
    CARD_TRANSACTION_DEBIT_FAILED(60002, "Oops! Failed to debit wallet!"),
    CARD_CVV_INVALID(60003, "Oops! Card CVV invalid!"),
    CARD_TRANSACTION_INIT_OK(60004, "Oops! Card transaction init successful!"),
    CARD_VALIDATION_FAILED(60005, "Oops! Card validation failed! Please make sure card details are correct!"),
    CARD_TRANSACTION_INIT_FAILED(60006, "Oops! Card init failed, please try again later or contact support!"),
    CARD_TRANSACTION_OK(60007, "Card transaction was successful!");

    private final int code;
    private final String message;

    ResponseCodeMapping(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
