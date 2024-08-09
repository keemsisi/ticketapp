package org.core.backend.ticketapp.plan.repository;

import org.core.backend.ticketapp.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID>, PagingAndSortingRepository<Plan, UUID> {
}
