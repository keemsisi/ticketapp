package org.core.backend.ticketapp.transaction.service;

import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.transaction.entity.request.PaymentRequest;

public interface PaymentRequestService extends IService<PaymentRequest> {
    PaymentRequest update(PaymentRequest paymentRequest);
}
