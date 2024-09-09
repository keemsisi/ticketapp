package org.core.backend.ticketapp.transaction.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.transaction.dto.BankResponse;
import org.core.backend.ticketapp.transaction.service.impl.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/api/v1/banks")
@RestController
@AllArgsConstructor
public class BankController {
    private final BankService bankService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankResponse> getBanks() {
        final var response = bankService.getBanks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
