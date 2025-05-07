package org.core.backend.ticketapp.transaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.dto.ApprovePaymentRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.SettlementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/settlements")
@RestController
@AllArgsConstructor
public class EventSettlementController {
    private final SettlementService settlementService;
    private final JwtTokenUtil jwtTokenUtil;


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/transfer")
    public ResponseEntity<GenericApiResponse<Transaction>> transfer(@RequestBody ApprovePaymentRequestDTO request) throws JsonProcessingException {
        final var verified = settlementService.processApprovedTransfer(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00",
                "Transfer was done successfully!", verified),
                HttpStatus.OK);
    }
}
