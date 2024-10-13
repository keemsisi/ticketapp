package org.core.backend.ticketapp.marketing.repository;

import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocialMediaLinksAdvertisementRepository extends JpaRepository<SocialMediaLinkAdvertisement, UUID> {
    @NotNull
    @Query(value = "SELECT * FROM social_media_link_advertisement WHERE id = ?1 AND deleted = false ", nativeQuery = true)
    Optional<SocialMediaLinkAdvertisement> findById(final UUID uuid);

    @NotNull
    @Query(value = "SELECT * FROM social_media_link_advertisement WHERE id = ?1 AND user_id = ?2 AND deleted = false ", nativeQuery = true)
    Optional<SocialMediaLinkAdvertisement> findById(final @NotNull UUID uuid, final @NotNull UUID userId);

    @Query(value = "SELECT * FROM social_media_link_advertisement WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<SocialMediaLinkAdvertisement> findAll(final @NotNull UUID userId, final @NotNull Pageable pageable);

    @NotNull
    @Query(value = "SELECT * FROM social_media_link_advertisement WHERE deleted = false ", nativeQuery = true)
    Page<SocialMediaLinkAdvertisement> findAll(final @NotNull Pageable pageable);
}
