package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.event.entity.Event;
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
public interface EventRepository extends JpaRepository<Event, UUID>, PagingAndSortingRepository<Event, UUID> {
    @NotNull
    @Query(value = "SELECT e.* FROM event e WHERE e.id = ?1 AND e.deleted=false", nativeQuery = true)
    Optional<Event> findById(final @NotNull UUID id);

    @NotNull
    @Query(value = "SELECT e.* FROM event e WHERE e.id = ?1 AND e.tenant_id=?2 AND e.deleted=false", nativeQuery = true)
    Optional<Event> findById(final @NotNull UUID id, final UUID tenantId);

    @Query(value = "SELECT e.* FROM event e WHERE e.ticket_type = ?1 AND e.deleted=false", nativeQuery = true)
    Page<Event> getByEventTicketType(@NotNull String eventTicketType, final Pageable pageRequest);
}
