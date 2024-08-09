package org.core.backend.ticketapp.plan.service;

import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.plan.entity.Plan;

import java.util.List;

public interface PlanService {
    List<Plan> getAllPlans();
    PlanDTO fetchPlans() throws Exception;
    Plan createPlan(PlanCreateRequestDTO createPlanDto) throws Exception;
}
