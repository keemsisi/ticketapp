package org.core.backend.ticketapp.transaction.service.client;

import org.core.backend.ticketapp.event.dto.EventCategoryCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentInitResponseDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentVerificationResponseDTO;
import org.core.backend.ticketapp.transaction.service.client.paystack.PayStackApiServiceProxyFallback;
import org.core.backend.ticketapp.transaction.service.payment.paystack.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Component
@FeignClient(name = "payStackApiServiceProxy", url = "${system.payment.vendor.paystack.baseUrl}", fallback = PayStackApiServiceProxyFallback.class)
public interface PayStackApiServiceProxy {

    @RequestMapping(value = "/plan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PlanCreateResponseDTO> createPlan(@Valid @RequestBody PlanCreateRequestDTO request, @RequestHeader("Authorization") String token);

    @RequestMapping(value = "/transaction/verify/:reference", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentVerificationResponseDTO> verifyPayment(final @PathVariable String reference, @RequestHeader("Authorization") String token);

    @RequestMapping(value = "/transfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TransferResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO request, @RequestHeader("Authorization") String token);

    @RequestMapping(value = "/transfer/finalize_transfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<FinalizeTransferResponseDTO> finalizeTransfer(@Valid @RequestBody FinalizeTransferRequestDTO request, @RequestHeader("Authorization") String token);

    @RequestMapping(value = "/transferrecipient", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TransferRecipientResponseDTO> createTransferRecipient(@Valid @RequestBody TransferRecipientRequestDTO request, @RequestHeader("Authorization") String token);

    @RequestMapping(value = "/subscription", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    default ResponseEntity<?> createSubscription(@Valid @RequestBody Object request, @RequestHeader("Authorization") String token) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(value = "/transaction/initialize", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentInitResponseDTO> initTransaction(@Valid @RequestBody EventCategoryCreateRequestDTO request,
                                                           @RequestHeader("Authorization") String token);
}