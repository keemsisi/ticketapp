package org.core.backend.ticketapp.transaction.controller;

import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transaction")
public record TransactionController(TransactionService transactionService) implements ICrudController {
}
