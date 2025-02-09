package org.core.backend.ticketapp.transaction.service.wallet;


import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.transaction.dto.wallet.request.CreateWalletDTO;
import org.core.backend.ticketapp.transaction.dto.wallet.request.WalletUpdateRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;
import org.core.backend.ticketapp.transaction.entity.wallet.WalletType;

import java.util.UUID;

public interface WalletService extends IService<Wallet> {
    Wallet createWallet(final CreateWalletDTO request);

    Wallet getWalletById(UUID walletId);

    Wallet deleteWalletById(final UUID id);

    Wallet updateWallet(final WalletUpdateRequestDTO updateRequestDTO);

    void save(Wallet wallet);

    void creditWallet(Transaction transaction, Wallet wallet);

    void debitWallet(Transaction transaction, Wallet wallet);

    Wallet getOrCreateWallet(UUID userId, WalletType walletType, String currency);
}
