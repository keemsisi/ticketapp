package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Tenant;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Transactional
@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID>, PagingAndSortingRepository<Tenant, UUID> {

    @Query(value = "SELECT * FROM tenant WHERE id = :tenantId LIMIT 1", nativeQuery = true)
    Optional<Tenant> findRegistrar(@Param("tenantId") UUID tenantId);

    @Query(value = "SELECT * FROM tenant WHERE name LIKE CONCAT('%',:name,'%') " +
            " AND deleted=false AND subscription_expiry_date >= now() ", nativeQuery = true)
    Page<Tenant> getAll(@Param("name") String name, Pageable pageable);

    @NotNull
    @Query(value = "SELECT * FROM tenant WHERE id = ?1 LIMIT 1", nativeQuery = true)
    Optional<Tenant> findById(@NotNull UUID id);

}
