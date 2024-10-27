package org.core.backend.ticketapp.ticket.repository;

import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID>, PagingAndSortingRepository<QrCode, UUID> {
    @Query(value = "SELECT * FROM qr_code WHERE id = ?1 AND tenant_id=?2 AND deleted=false ", nativeQuery = true)
    Optional<QrCode> getById(final UUID id, final UUID tenantId);

    @Query(value = "SELECT * FROM qr_code t WHERE t.event_id = ?1 AND t.tenant_id = ?2 ", nativeQuery = true)
    Page<QrCode> findByEventIdAndTenantId(final UUID eventId, final UUID tenantId, final Pageable pageRequest);

    @Query(value = "SELECT * FROM qr_code t WHERE t.tenant_id = ?1 ", nativeQuery = true)
    Page<QrCode> findByTenantId(final UUID tenantId, final Pageable pageRequest);

    @Query(value = "SELECT * FROM qr_code t WHERE t.event_id = ?1 AND t.tenant_id = ?2 AND t.user_id = ?3 ", nativeQuery = true)
    Page<QrCode> findByEventIdAndTenantIdAndUserId(final UUID eventId, final UUID tenantId,
                                                   final UUID userId, final Pageable pageRequest);

    @Query(value = "SELECT * FROM qr_code t WHERE t.tenant_id = ?1 AND t.user_id = ?2 AND t.deleted=false ", nativeQuery = true)
    Page<QrCode> findByTenantIdAndUserId(final UUID tenantId, final UUID userId, final Pageable pageRequest);

    @Query(value = "SELECT * FROM qr_code t WHERE t.user_id = ?1 AND t.deleted=false ", nativeQuery = true)
    Page<QrCode> findByUserId(final UUID userId, final Pageable pageable);


    @Query(value = "SELECT * FROM qr_code t WHERE t.code = ?1 AND t.deleted=false ", nativeQuery = true)
    Optional<QrCode> getByCode(final String code);
}
