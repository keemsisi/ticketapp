package org.core.backend.ticketapp.transaction.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.transaction.dto.PaystackWebhookEvent;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController implements ICrudController {
    TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Page<Transaction>>> getAll(Pageable pageable) {
        final var transactions = transactionService.getAll(pageable);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "All transactions", transactions), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/verify")
    public ResponseEntity<GenericApiResponse<Transaction>> verifyPayment(@RequestBody TransactionVerifyRequestDTO request) throws Exception {
        final var verified = transactionService.verifyPayment(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Payment verified successfully", verified), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/paystack/webhook")
    public ResponseEntity<GenericApiResponse<Object>> paystackWebhook(@RequestBody PaystackWebhookEvent request) throws Exception {
        transactionService.processPaystackWebhook(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Paystack event processed successfully", null), HttpStatus.OK);
    }

}
