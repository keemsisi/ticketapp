package org.core.backend.ticketapp.transaction.service.wallet;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.Status;
import org.core.backend.ticketapp.transaction.dto.wallet.request.WalletTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;
import org.core.backend.ticketapp.transaction.service.impl.TransactionServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {
    private final TransactionServiceImpl transactionService;
    private final WalletService walletService;
    private final ObjectMapper objectMapper;

    @Override
    public Transaction getById(UUID transactionId) {
        return transactionService.getById(transactionId);
    }


    @Override
    public Transaction initAndProcess(final WalletTransactionRequestDTO request) throws JsonProcessingException {
        final var senderWallet = walletService.getWalletById(request.getSenderWalletId());
        final var receiverWallet = walletService.getWalletById(request.getReceiverWalletId());
        final var transaction = create(request, senderWallet, receiverWallet);
        walletService.debitWallet(transaction, senderWallet);
        walletService.creditWallet(transaction, receiverWallet);
        transaction.setDateModified(LocalDateTime.now());
        transaction.setStatus(Status.COMPLETED);
        save(transaction);
        return transaction;
    }

    @Override
    public Transaction create(final WalletTransactionRequestDTO request, final Wallet senderWallet, final Wallet receiverWallet) throws JsonProcessingException {
        final var meta = objectMapper.writeValueAsString(request);
        final var transaction = Transaction.builder()
                .amount(request.getAmount())
                .senderAccountId(senderWallet.getAccountNumber())
//                .senderName(senderWallet.getAccountName())
//                .receiverAccountId(receiverWallet.getAccountNumber())
//                .receiverName(receiverWallet.getAccountName())
//                .fee(BigDecimal.TEN)
                .amount(request.getAmount())
//                .channel(TransactionChannel.WALLET)
//                .dateCompleted(LocalDateTime.now())
                .status(Status.PENDING)
//                .metaData(meta)
                .build();
        transaction.setDateCreated(LocalDateTime.now());
        return transactionService.save(transaction);
    }

    @Override
    public Transaction save(final Transaction transaction) {
        return transactionService.save(transaction);
    }
}
