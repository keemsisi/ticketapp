package org.core.backend.ticketapp.transaction.repository;

import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID>, PagingAndSortingRepository<Wallet, UUID> {

}

