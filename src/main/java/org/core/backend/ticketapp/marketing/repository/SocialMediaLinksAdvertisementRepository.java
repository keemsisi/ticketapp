package org.core.backend.ticketapp.marketing.repository;

import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocialMediaLinksAdvertisementRepository extends JpaRepository<SocialMediaLinkAdvertisement, UUID> {
    @NotNull
    @Query(value = "SELECT * FROM social_media_link_advertisement WHERE id = ?1 AND deleted = false ", nativeQuery = true)
    Optional<SocialMediaLinkAdvertisement> findById(@NotNull UUID uuid);

    @Query(value = "SELECT * FROM social_media_link_advertisement WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<SocialMediaLinkAdvertisement> findAll(@NotNull UUID userId, @NotNull Pageable pageable);

    @NotNull
    @Query(value = "SELECT * FROM social_media_link_advertisement WHERE deleted = false ", nativeQuery = true)
    Page<SocialMediaLinkAdvertisement> findAll(@NotNull Pageable pageable);
}
