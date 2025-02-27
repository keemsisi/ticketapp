package org.core.backend.ticketapp.transaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.marketing.entity.CustomerFormData;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.transaction.dto.CreatePaymentRequestDTO;
import org.core.backend.ticketapp.transaction.dto.payment_request.UpdatePaymentRequestRequestDTO;
import org.core.backend.ticketapp.transaction.entity.request.PaymentRequest;
import org.core.backend.ticketapp.transaction.service.PaymentRequestService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/payment-request")
@AllArgsConstructor
public class PaymentRequestController {
    private final PaymentRequestService service;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PaymentRequest>> request(@RequestBody CreatePaymentRequestDTO request) throws JsonProcessingException {
        if (!request.getType().isEventSettlement()) {
            return new ResponseEntity<>(new GenericApiResponse<>("01",
                    "Payment request type not allowed", null),
                    HttpStatus.FORBIDDEN);
        }
        final var response = service.create(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00",
                "Payment request created successfully, please hold for processing!", response),
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PaymentRequest>> getById(final @PathVariable UUID id) {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var result = service.getById(id);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully fetched resource", result));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var result = PagedMapperUtil.map(service.getAll(pageable));
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully fetched resource", result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<CustomerFormData>> deleteById(final @PathVariable UUID id) {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        service.delete(id);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully deleted resource!", null));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PaymentRequest>> update(final @RequestBody UpdatePaymentRequestRequestDTO request) {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var result = service.update(request);
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully updated resource!", result));
    }
}
