package org.core.backend.ticketapp.transaction.repository;

import org.core.backend.ticketapp.transaction.entity.request.PaymentRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, UUID>, PagingAndSortingRepository<PaymentRequest, UUID> {
    @Query(value = "SELECT e.* FROM payment_request e WHERE e.user_id = ?1 AND e.type=?2 AND e.deleted=false ", nativeQuery = true)
    List<PaymentRequest> findByUserIdAndType(final UUID userId, final String type);

    @NotNull
    @Query(value = "SELECT e.* FROM payment_request e WHERE e.id = ?1 AND e.deleted=false ", nativeQuery = true)
    Optional<PaymentRequest> findById(final @NotNull UUID id);

    @NotNull
    @Query(value = "SELECT e.* FROM payment_request e WHERE e.id = ?1 AND e.user_id=?2 AND e.deleted=false ", nativeQuery = true)
    Optional<PaymentRequest> findByIdAndUserId(final @NotNull UUID id, final UUID userId);

    @Query(value = "SELECT * FROM payment_request WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<PaymentRequest> findAll(final @javax.validation.constraints.NotNull UUID userId, final @javax.validation.constraints.NotNull Pageable pageable);
}