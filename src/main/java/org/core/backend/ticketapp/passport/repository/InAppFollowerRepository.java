package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.InAppFollower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InAppFollowerRepository extends JpaRepository<InAppFollower, UUID> {
    @NotNull
    @Query(value = "SELECT * FROM in_app_follower WHERE id = ?1 AND deleted = false ", nativeQuery = true)
    Optional<InAppFollower> findById(final UUID uuid);

    @NotNull
    @Query(value = "SELECT * FROM in_app_follower WHERE id = ?1 AND user_id = ?2 AND deleted = false ", nativeQuery = true)
    Optional<InAppFollower> findById(final @NotNull UUID uuid, final @NotNull UUID userId);

    @Query(value = "SELECT * FROM in_app_follower WHERE user_id = ?1 AND deleted = false ", nativeQuery = true)
    Page<InAppFollower> findAll(final @NotNull UUID userId, final @NotNull Pageable pageable);

    @NotNull
    @Query(value = "SELECT * FROM in_app_follower WHERE deleted = false ", nativeQuery = true)
    Page<InAppFollower> findAll(final @NotNull Pageable pageable);
}
