package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountDetailsRepository extends JpaRepository<BankAccountDetails, UUID>, PagingAndSortingRepository<BankAccountDetails, UUID> {
    @NotNull
    @Query(value = "SElECT * FROM bank_account_details bad WHERE bad.id=?1 AND bad.deleted=false", nativeQuery = true)
    Optional<BankAccountDetails> findById(final @NotNull UUID id);

    @NotNull
    @Query(value = "SElECT * FROM bank_account_details bad WHERE bad.user_id=?1 AND bad.deleted=false", nativeQuery = true)
    Optional<BankAccountDetails> findByUserId(final @NotNull UUID userId);

    @Query(value = "SElECT * FROM bank_account_details bad WHERE bad.deleted=false", nativeQuery = true)
    Page<BankAccountDetails> getAll(final @NotNull Pageable pageable);

}
