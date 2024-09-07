package org.core.backend.ticketapp.event.repository;

import lombok.extern.java.Log;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface EventSeatSectionRepository extends JpaRepository<EventSeatSection, UUID>, PagingAndSortingRepository<EventSeatSection, UUID> {
    @NotNull
    @Query(value = "SELECT e.* FROM event_seat_sections e WHERE e.id = ?1 AND e.deleted=false", nativeQuery = true)
    Optional<EventSeatSection> findById(final @NotNull UUID id);

    @NotNull
    @Query(value = "SELECT sum(e.capacity) FROM event_seat_sections e WHERE e.id = ?1 AND e.deleted=false", nativeQuery = true)
    Optional<Long> getTotalTickets(final @NotNull UUID id);

    @Query(value = "SELECT * FROM event_seat_sections e WHERE e.event_id = ?1 AND e.deleted=false", nativeQuery = true)
    List<EventSeatSection> getAllByEventId(final UUID eventId);
}
