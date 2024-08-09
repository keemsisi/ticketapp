package org.core.backend.ticketapp.transaction.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.event.dto.EventCategoryCreateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventCategory;
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyResponseDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController implements ICrudController {
    TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<List<Transaction>>> getAll() throws Exception {
        final var transactions = transactionService.getAll();
        return new ResponseEntity<>(new GenericResponse<>("00", "All transactions", transactions), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/initialize-payment")
    public ResponseEntity<GenericResponse<TransactionInitializeResponseDTO>> initializePayment(@Validated @RequestBody TransactionInitializeRequestDTO request) throws Throwable {
        final var initializedPayment = transactionService.initializePayment(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Payment initialized successfully", initializedPayment), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/verify-payment")
    public ResponseEntity<GenericResponse<TransactionVerifyResponseDTO>> verifyPayment(@Validated @RequestBody TransactionVerifyRequestDTO request) throws Exception {
        final var verified = transactionService.verifyPayment(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Payment verified successfully", verified), HttpStatus.OK);
    }

}
