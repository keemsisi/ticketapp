package org.core.backend.ticketapp.transaction.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.dto.BankResponse;
import org.core.backend.ticketapp.transaction.service.impl.BankAccountDetailsService;
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
    private final BankAccountDetailsService bankAccountDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankResponse> getBanks() {
        final var response = bankAccountDetailsService.getBanks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
