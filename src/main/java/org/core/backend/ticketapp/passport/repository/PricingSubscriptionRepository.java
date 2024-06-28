package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.PricingSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Transactional
@Repository
public interface PricingSubscriptionRepository extends JpaRepository<PricingSubscription, UUID>, PagingAndSortingRepository<PricingSubscription, UUID> {

    @Deprecated
    @Query(value = "SELECT * FROM pricing_subscription WHERE COALESCE(LOWER(name),'') LIKE CONCAT('%', :name,'%') " +
            " ORDER BY created_on DESC ",
            nativeQuery = true)
    Page<PricingSubscription> getAll(String name, Pageable pageable);

    @Query(value = "SELECT * FROM pricing_subscription WHERE tenant_id=:tenantId AND COALESCE(LOWER(name),'') LIKE CONCAT('%', :name,'%') " +
            " ORDER BY created_on DESC ",
            nativeQuery = true)
    List<PricingSubscription> getAll(String name, UUID tenantId);

    @Query(value = "SELECT * FROM pricing_subscription WHERE LOWER(name) = Lower(?1) ", nativeQuery = true)
    Optional<PricingSubscription> findByName(String name);

    @Query(value = "SELECT * FROM pricing_subscription WHERE id = ?1", nativeQuery = true)
    Optional<PricingSubscription> findByUUID(UUID id);
}
