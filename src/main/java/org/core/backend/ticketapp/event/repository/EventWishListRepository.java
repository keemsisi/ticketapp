package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.event.entity.EventWishList;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventWishListRepository extends JpaRepository<EventWishList, UUID>, PagingAndSortingRepository<EventWishList, UUID> {
    @NotNull
    @Query(value = "SELECT e.* FROM event_wishlist e WHERE e.id = ?1 AND e.deleted=false AND e.user_id=?2", nativeQuery = true)
    Optional<EventWishList> getById(final @NotNull UUID id, UUID userId);

    @NotNull
    @Query(value = "SELECT e.* FROM event_wishlist e WHERE e.event_id = ?1 AND e.deleted=false AND e.user_id=?2", nativeQuery = true)
    List<EventWishList> getByEventIdAndUserId(final @NotNull UUID eventId, UUID userId);
}
