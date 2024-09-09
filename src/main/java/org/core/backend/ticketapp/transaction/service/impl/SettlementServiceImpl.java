package org.core.backend.ticketapp.transaction.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.core.backend.ticketapp.transaction.dto.TransferRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.SettlementService;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
public class SettlementServiceImpl implements SettlementService {
    private final TicketService ticketService;
    private final EventService eventService;
    private final CoreUserService coreUserService;
    private final OrderService orderService;
    private final TransactionService transactionService;
    private final BankAccountDetailsService bankAccountDetailsService;
    private final PaymentProcessorService paymentProcessorService;

    @Override
    public Transaction transfer(final TransferRequestDTO request) {
        final var bankAccountDetails = bankAccountDetailsService.getByUserId(request.getUserId());
        return null;
    }
}
