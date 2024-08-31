package org.core.backend.ticketapp.plan.repository;

import org.core.backend.ticketapp.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID>, PagingAndSortingRepository<Plan, UUID> {
    @NotNull
    @Query(value = "SELECT * FROM plan WHERE id = ?1 AND deleted=false", nativeQuery = true)
    Optional<Plan> findById(@NotNull UUID planId);

    @Query(value = "SELECT * FROM plan p WHERE p.plan_code = ?1 AND p.deleted=false", nativeQuery = true)
    Optional<Plan> findByCode(String code);
}
