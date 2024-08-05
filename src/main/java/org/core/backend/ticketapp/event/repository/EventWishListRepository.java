package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.event.entity.EventWishList;
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
public interface EventWishListRepository extends JpaRepository<EventWishList, UUID>, PagingAndSortingRepository<EventWishList, UUID> {
    @NotNull
    @Query(value = "SELECT e.* FROM event_wishlist e WHERE e.id = ?1 AND e.deleted=false AND e.user_id=?2", nativeQuery = true)
    Optional<EventWishList> getById(final @NotNull UUID id, @NotNull final UUID userId);

    @NotNull
    @Query(value = "SELECT e.* FROM event_wishlist e WHERE e.deleted=false AND e.user_id=?1", nativeQuery = true)
    Page<EventWishList> findAll(@NotNull final UUID userId, @NotNull final Pageable pageable);
}
