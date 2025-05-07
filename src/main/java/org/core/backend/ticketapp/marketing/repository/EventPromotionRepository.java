package org.core.backend.ticketapp.marketing.repository;

import org.core.backend.ticketapp.marketing.entity.EventPromotion;
import org.core.backend.ticketapp.marketing.entity.SponsoredOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventPromotionRepository extends JpaRepository<EventPromotion, UUID> {
    @Query(value = "SELECT * FROM event_promotion WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<EventPromotion> findAll(final @NotNull UUID userId, final @NotNull Pageable pageable);

    @Query(value = "SELECT * FROM event_promotion WHERE deleted = false AND end_date >= now() ", nativeQuery = true)
    @org.jetbrains.annotations.NotNull
    Page<EventPromotion> findAll(@NotNull @org.jetbrains.annotations.NotNull Pageable pageable);

    @Query(value = "SELECT * FROM event_promotion WHERE id = ?1 AND user_id = ?2 AND deleted = false ", nativeQuery = true)
    Optional<EventPromotion> findById(UUID id, UUID userId);
}
