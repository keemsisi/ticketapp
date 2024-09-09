package org.core.backend.ticketapp.common.util;

public interface Constants {
    String[] supportedActions = {
            "CREATE",
            "UPDATE",
            "VIEW",
            "DELETE",
            "AUTHORIZE REQUEST",
            "DECLINE REQUEST",
            "PRINT REPORT"
    };

    String PAYSTACK_PLAN_BASE_URL = "https://api.paystack.co/plan";
    String PAYSTACK_SUBSCRIPTION_BASE_URL = "https://api.paystack.co/subscription";
    String PAYSTACK_INITIALIZE_PAY = "https://api.paystack.co/transaction/initialize";
    String PAYSTACK_VERIFY = "https://api.paystack.co/transaction/verify/%s";
    String PAYSTACK_TRANSFER = "https://api.paystack.co/transfer";
    String PAYSTACK_RECIPIENT_TRANSFER = "https://api.paystack.co/transferrecipient";
}
