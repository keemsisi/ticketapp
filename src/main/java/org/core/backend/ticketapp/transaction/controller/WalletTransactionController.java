package org.core.backend.ticketapp.transaction.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.transaction.dto.wallet.request.WalletTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.wallet.WalletTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/v1/api/transaction/wallet")
public class WalletTransactionController {
    private final WalletTransactionService transactionService;

    @PreAuthorize("hasAuthority('WALLET_TRANSFER')")
    @RequestMapping(value = "/transfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Transaction>> initAndProcess(@Valid @RequestBody WalletTransactionRequestDTO request) throws JsonProcessingException {
        final var transaction = transactionService.initAndProcess(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Processed transaction successfully", transaction), HttpStatus.OK);
    }

}
