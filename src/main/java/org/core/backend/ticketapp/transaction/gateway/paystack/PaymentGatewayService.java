package org.core.backend.ticketapp.transaction.gateway.paystack;

public interface PaymentGatewayService {
    void createOrder();

    void verifyPayment();

    void createPlan();

    void createSubscription();

    void fetchSubscriptions();

    void getPlanById();

    void updatePlan();

    void transfer();

    void debit();
}
