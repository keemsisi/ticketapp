package org.core.backend.ticketapp.transaction.service.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.transaction.dto.wallet.request.WalletTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;

public interface WalletTransactionService extends IService<Transaction> {

    Transaction initAndProcess(WalletTransactionRequestDTO request) throws JsonProcessingException;


    Transaction create(WalletTransactionRequestDTO request, Wallet sender, Wallet receiver) throws JsonProcessingException;

    Transaction save(Transaction transaction);
}
