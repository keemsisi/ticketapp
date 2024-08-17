package org.core.backend.ticketapp.plan.service;

import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.plan.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlanService {
    Page<Plan> getAllPlans(Pageable pageable);
    PlanDTO fetchPlans() throws Exception;
    Plan createPlan(PlanCreateRequestDTO createPlanDto) throws Exception;
}
