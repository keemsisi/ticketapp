package org.core.backend.ticketapp.transaction.repository;

import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, PagingAndSortingRepository<Transaction, UUID> {
    @Query(value = "SELECT e.* FROM transaction e WHERE e.reference = ?1 AND e.deleted=false", nativeQuery = true)
    Optional<Transaction> findByReference(String reference);
}
