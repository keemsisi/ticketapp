package org.core.backend.ticketapp.ticket.repository;

import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID>, PagingAndSortingRepository<Ticket, UUID> {
    @Query(value = "SELECT * FROM ticket WHERE id = ?1 AND tenant_id=?2 AND deleted=false ", nativeQuery = true)
    Optional<Ticket> getById(final UUID id, final UUID tenantId);

    @Query(value = "SELECT * FROM ticket t WHERE t.event_id = ?1 AND t.tenant_id = ?2 ")
    Page<Ticket> findByEventIdAndTenantId(final UUID uuid, final UUID tenantId, final Pageable pageRequest);

    @Query(value = "SELECT * FROM ticket t WHERE t.tenant_id = ?1 ")
    Page<Ticket> findByTenantId(final UUID tenantId, final PageRequest pageRequest);
}
