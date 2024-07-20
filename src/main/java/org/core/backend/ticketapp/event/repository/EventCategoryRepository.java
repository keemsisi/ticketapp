package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.event.entity.EventCategory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, UUID>, PagingAndSortingRepository<EventCategory, UUID> {
    @Query(value = "SELECT e.* FROM event_category e WHERE e.name = ?1", nativeQuery = true)
    Optional<EventCategory> getByName(final @NotNull String name);

    @Query(value = "SELECT e.* FROM event_category e WHERE e.name IN ?1 AND e.deleted=false", nativeQuery = true)
    Set<EventCategory> findAllByName(final List<String> eventCategories);

    @Query(value = "SELECT e.* FROM event_category e WHERE e.id IN ?1 AND e.deleted=false", nativeQuery = true)
    List<EventCategory> getAllByIds(final List<UUID> ids);
}
