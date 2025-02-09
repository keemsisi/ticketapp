package org.core.backend.ticketapp.transaction.service.wallet;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.transaction.dto.wallet.request.CreateWalletDTO;
import org.core.backend.ticketapp.transaction.dto.wallet.request.WalletUpdateRequestDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;
import org.core.backend.ticketapp.transaction.entity.wallet.WalletType;
import org.core.backend.ticketapp.transaction.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    private static final String REF_TEMPLATE = "WAL_REF_%s";
    private final WalletRepository walletRepository;
    private final CoreUserService coreUserService;

    @Override
    public Wallet createWallet(final CreateWalletDTO request) {
        final var ref = String.format(REF_TEMPLATE, System.currentTimeMillis());
        final var wallet = new Wallet();
        coreUserService.getUserById(request.getUserId()).orElseThrow(ApplicationException::notFoundException);
        wallet.setType(WalletType.PRIMARY);
        wallet.setReference(ref);
        wallet.setAccountName(request.getName());
        wallet.setUserId(request.getUserId());
        wallet.setAccountNumber(RandomStringUtils.randomNumeric(10));
        wallet.setDateCreated(LocalDateTime.now());
        return walletRepository.save(wallet);
    }


    @Override
    public Wallet getWalletById(final UUID walletId) {
        final var wallet = walletRepository.findById(walletId).orElse(null);
        if (Objects.isNull(wallet)) {
            throw new ApplicationException(404, "not_found", "Oops! Wallet not found!");
        }
        return wallet;
    }

    @Override
    public Wallet deleteWalletById(final UUID id) {
        final var wallet = getWalletById(id);
        walletRepository.delete(wallet);
        return wallet;
    }

    @Override
    public Wallet updateWallet(final WalletUpdateRequestDTO updateRequestDTO) {
        final var wallet = getWalletById(updateRequestDTO.getWalletId());
        wallet.setAccountName(updateRequestDTO.getAccountName());
        wallet.setBalance(updateRequestDTO.getBalance());
        wallet.setBalanceBefore(updateRequestDTO.getBalanceBefore());
        return walletRepository.save(wallet);
    }

    @Override
    public void save(final Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public void creditWallet(final Transaction transaction, final Wallet wallet) {
        final var balanceBefore = wallet.getBalance();
        final var balanceAfter = wallet.getBalance().add(transaction.getAmount());
        wallet.setBalance(balanceAfter);
        wallet.setBalanceBefore(balanceBefore);
        wallet.setDateModified(LocalDateTime.now());
        wallet.setLastTransactionDate(LocalDateTime.now());
        walletRepository.save(wallet);
        log.info(">>> User wallet account[accountNumber: {}] credited with {} ", wallet.getAccountNumber(), transaction.getAmount());
    }

    @Override
    public void debitWallet(final Transaction transaction, final Wallet wallet) {
        final var balanceBefore = wallet.getBalance();
        final var balanceAfter = wallet.getBalance().subtract(transaction.getAmount()).add(BigDecimal.ZERO);
        wallet.setBalance(balanceAfter);
        wallet.setBalanceBefore(balanceBefore);
        wallet.setDateModified(LocalDateTime.now());
        wallet.setLastTransactionDate(LocalDateTime.now());
        walletRepository.save(wallet);
        log.info(">>> User wallet account[accountNumber: {}] debited with {} ", wallet.getAccountNumber(), transaction.getAmount());
    }

    @Override
    public Wallet getOrCreatedWallet(final UUID userId, final WalletType walletType) {
        return walletRepository.findByUserIdAndType(userId, walletType).orElseGet(() -> {
            final var request = CreateWalletDTO.builder().userId(userId).name(RandomStringUtils.randomAlphanumeric(10) + "_" + WalletType.COIN_WALLET).build();
            return createWallet(request);
        });
    }
}
