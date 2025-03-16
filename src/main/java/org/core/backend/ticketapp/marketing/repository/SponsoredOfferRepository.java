package org.core.backend.ticketapp.marketing.repository;

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
public interface SponsoredOfferRepository extends JpaRepository<SponsoredOffer, UUID> {
    @Query(value = "SELECT * FROM sponsored_offer WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<SponsoredOffer> findAll(final @NotNull UUID userId, final @NotNull Pageable pageable);

    @Query(value = "SELECT * FROM sponsored_offer WHERE id = ?1 AND user_id = ?2 AND deleted = false ", nativeQuery = true)
    Optional<SponsoredOffer> findById(UUID id, UUID userId);

    @Query(value = "SELECT * FROM sponsored_offer WHERE code = ?1 AND deleted = false ", nativeQuery = true)
    Optional<SponsoredOffer> findByCode(String code);
}
