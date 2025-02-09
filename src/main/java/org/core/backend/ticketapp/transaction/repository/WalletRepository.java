package org.core.backend.ticketapp.transaction.repository;

import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID>, PagingAndSortingRepository<Wallet, UUID> {
    @Query(value = "SELECT e.* FROM wallet e WHERE e.user_id = ?1 AND e.type=?2 AND e.deleted=false ", nativeQuery = true)
    Optional<Wallet> findByUserIdAndType(final UUID userId, final String walletType);
}

