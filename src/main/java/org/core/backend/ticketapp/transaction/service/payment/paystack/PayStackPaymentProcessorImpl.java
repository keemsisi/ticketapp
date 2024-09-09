package org.core.backend.ticketapp.transaction.service.payment.paystack;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.transaction.service.client.PayStackApiServiceProxy;
import org.core.backend.ticketapp.transaction.service.payment.PaymentProcessorService;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentRequestDTO;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentResponseDTO;
import org.core.backend.ticketapp.transaction.service.payment.paystack.dto.TransferRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class PayStackPaymentProcessorImpl implements PaymentProcessorService {
    private final PayStackApiServiceProxy payStackApiServiceProxy;
    private final AppConfigs appConfigs;
    private final String BEARER = "Bearer ";

    @Override
    public ProcessorPaymentResponseDTO transfer(final ProcessorPaymentRequestDTO request) {
        return switch (request.getPaymentProcessorType()) {
            case PAYSTACK -> initPaymentWithPayStack(request);
        };
    }

    private ProcessorPaymentResponseDTO initPaymentWithPayStack(final ProcessorPaymentRequestDTO request) {
        final var response = payStackApiServiceProxy.transfer((TransferRequestDTO) request, BEARER + appConfigs.payStackApiKey);
        Objects.requireNonNull(response.getBody());
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info(">>> processPaymentWithPayStack request: {} response : {} ", request, response);
            return response.getBody();
        } else {
            log.info(">>> Failed request response from paystack payment processor {} ", response.getBody());
            throw new ApplicationException(response.getStatusCodeValue(), "error", "Unprocessed from payment processor");
        }
    }
}
