package org.core.backend.ticketapp.transaction.service.payment.paystack;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.transaction.dto.BankResponse;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.core.backend.ticketapp.transaction.service.client.PayStackApiServiceProxy;
import org.core.backend.ticketapp.transaction.service.payment.PaymentProcessorService;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentRequestDTO;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentResponseDTO;
import org.core.backend.ticketapp.transaction.service.payment.paystack.dto.TransferRecipientRequestDTO;
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
    public ProcessorPaymentResponseDTO transfer(final ProcessorPaymentRequestDTO request, final BankAccountDetails bankAccountDetails) {
        return switch (request.getPaymentProcessorType()) {
            case PAYSTACK -> initPaymentWithPayStack(request, bankAccountDetails);
        };
    }

    @Override
    public BankResponse getBanks() {
        return payStackApiServiceProxy.getBanks(getToken()).getBody();
    }

    private ProcessorPaymentResponseDTO initPaymentWithPayStack(final ProcessorPaymentRequestDTO request, final BankAccountDetails bankAccountDetails) {
        final var transactionRequest = (TransferRequestDTO) request;
        final var recipient = transactionRequest.getRecipient();
        if (StringUtils.isBlank(recipient)) {
            final var transactionRecipientRequest = TransferRecipientRequestDTO.builder().build();
            transactionRecipientRequest.setType("customer");
            transactionRecipientRequest.setName(bankAccountDetails.getAccountName());
            transactionRecipientRequest.setCurrency(bankAccountDetails.getCurrency());
            transactionRecipientRequest.setAccountNumber(bankAccountDetails.getAccountNumber());
            transactionRecipientRequest.setBankCode(bankAccountDetails.getBankCode());
            final var createdRecipientResponse = payStackApiServiceProxy.createTransferRecipient(transactionRecipientRequest, getToken());
            final var responseBody = Objects.requireNonNull(createdRecipientResponse.getBody());
            if (!responseBody.isStatus()) {
                log.info(">>> Failed request response from paystack payment create transaction recipient {} ", createdRecipientResponse.getBody());
                throw new ApplicationException(createdRecipientResponse.getStatusCodeValue(), "error", "Unprocessed from payment processor");
            }
            transactionRequest.setRecipient(responseBody.getData().getRecipientCode());
        }
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

    public String getToken() {
        return BEARER + appConfigs.payStackApiKey;
    }
}
