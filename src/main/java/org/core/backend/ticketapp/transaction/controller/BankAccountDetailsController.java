package org.core.backend.ticketapp.transaction.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.service.impl.BankAccountDetailsAccountDetailsServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/api/v1/bank-account-details")
@RestController
@AllArgsConstructor
public class BankAccountDetailsController {
    private final JwtTokenUtil jwtTokenUtil;
    private final BankAccountDetailsAccountDetailsServiceImpl bankAccountDetailsService;
}
