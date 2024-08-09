package org.core.backend.ticketapp.plan.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.plan.entity.Plan;
import org.core.backend.ticketapp.plan.service.PlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/plan")
public class PlanController implements ICrudController {

    PlanService planService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/create-plan")
    public ResponseEntity<GenericResponse<Plan>> createPlan(@Validated @RequestBody PlanCreateRequestDTO request) throws Exception {
        final var plan = planService.createPlan(request);
        return new ResponseEntity<>(new GenericResponse<>("00", "Plan created successfully", plan), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/get-plans")
    public ResponseEntity<GenericResponse<List<Plan>>> getAllPlans() {
        final var plans = planService.getAllPlans();
        return new ResponseEntity<>(new GenericResponse<>("00", "All plans", plans), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/fetch-plans")
    public ResponseEntity<GenericResponse<PlanDTO>> fetchPlans() throws Exception {
        final var plans = planService.fetchPlans();
        return new ResponseEntity<>(new GenericResponse<>("00", "All plans", plans), HttpStatus.OK);
    }
}
