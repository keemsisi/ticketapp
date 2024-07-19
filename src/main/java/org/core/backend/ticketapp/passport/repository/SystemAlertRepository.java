package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.SystemAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface SystemAlertRepository extends JpaRepository<SystemAlert, UUID>, PagingAndSortingRepository<SystemAlert, UUID> {
    @Query(value = "SELECT * FROM system_alert WHERE id =?1", nativeQuery = true)
    Optional<SystemAlert> get(@NotNull UUID id);

    @Query(value = "SELECT * FROM system_alert WHERE tenant_id =?1", nativeQuery = true)
    Optional<SystemAlert> findByTenantId(UUID tenantId);
}
