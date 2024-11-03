package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationConfigRepository extends JpaRepository<ApplicationConfig, UUID>, PagingAndSortingRepository<ApplicationConfig, UUID> {
    @NotNull
    @Query(value = "SELECT e.* FROM application_config e WHERE e.id = ?1 AND e.deleted=false", nativeQuery = true)
    Optional<ApplicationConfig> findById(final @NotNull UUID id);

}



